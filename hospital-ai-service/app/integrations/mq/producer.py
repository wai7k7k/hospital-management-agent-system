import pika
import json
from typing import Optional, Dict, Any
from app.utils.config import settings
from app.utils.logger import log
import asyncio


class RabbitMQProducer:
    """RabbitMQ 消息生产者"""

    def __init__(self):
        """初始化生产者"""
        self.connection = None
        self.channel = None
        self._connect()

    def _connect(self):
        """建立连接"""
        try:
            credentials = pika.PlainCredentials(
                settings.RABBITMQ_USER,
                settings.RABBITMQ_PASSWORD
            )

            parameters = pika.ConnectionParameters(
                host=settings.RABBITMQ_HOST,
                port=settings.RABBITMQ_PORT,
                virtual_host=settings.RABBITMQ_VHOST,
                credentials=credentials
            )

            self.connection = pika.BlockingConnection(parameters)
            self.channel = self.connection.channel()

            # 声明交换机
            self.channel.exchange_declare(
                exchange="hospital.events",
                exchange_type="topic",
                durable=True
            )

            log.info("RabbitMQ 生产者连接成功")

        except Exception as e:
            log.error(f"RabbitMQ 生产者连接失败: {str(e)}")
            raise

    async def publish(
        self,
        exchange: str,
        routing_key: str,
        message: Dict[str, Any]
    ) -> bool:
        """
        发布消息

        Args:
            exchange: 交换机名称
            routing_key: 路由键
            message: 消息内容

        Returns:
            是否成功
        """
        try:
            # 在线程池中执行同步操作
            loop = asyncio.get_event_loop()
            await loop.run_in_executor(
                None,
                self._publish_sync,
                exchange,
                routing_key,
                message
            )

            return True

        except Exception as e:
            log.error(f"发布消息失败: {str(e)}")
            return False

    def _publish_sync(
        self,
        exchange: str,
        routing_key: str,
        message: Dict[str, Any]
    ):
        """同步发布消息"""
        try:
            self.channel.basic_publish(
                exchange=exchange,
                routing_key=routing_key,
                body=json.dumps(message, ensure_ascii=False),
                properties=pika.BasicProperties(
                    delivery_mode=2,  # 持久化消息
                    content_type="application/json"
                )
            )

            log.info(f"消息发布成功 - routing_key: {routing_key}")

        except Exception as e:
            log.error(f"同步发布消息失败: {str(e)}")
            raise

    def close(self):
        """关闭连接"""
        if self.connection and not self.connection.is_closed:
            self.connection.close()
            log.info("RabbitMQ 生产者连接已关闭")


class RabbitMQConsumer:
    """RabbitMQ 消息消费者"""

    def __init__(self, queue_name: str, routing_key: str):
        """
        初始化消费者

        Args:
            queue_name: 队列名称
            routing_key: 路由键
        """
        self.queue_name = queue_name
        self.routing_key = routing_key
        self.connection = None
        self.channel = None
        self._connect()

    def _connect(self):
        """建立连接"""
        try:
            credentials = pika.PlainCredentials(
                settings.RABBITMQ_USER,
                settings.RABBITMQ_PASSWORD
            )

            parameters = pika.ConnectionParameters(
                host=settings.RABBITMQ_HOST,
                port=settings.RABBITMQ_PORT,
                virtual_host=settings.RABBITMQ_VHOST,
                credentials=credentials
            )

            self.connection = pika.BlockingConnection(parameters)
            self.channel = self.connection.channel()

            # 声明交换机
            self.channel.exchange_declare(
                exchange="hospital.events",
                exchange_type="topic",
                durable=True
            )

            # 声明队列
            self.channel.queue_declare(
                queue=self.queue_name,
                durable=True
            )

            # 绑定队列
            self.channel.queue_bind(
                exchange="hospital.events",
                queue=self.queue_name,
                routing_key=self.routing_key
            )

            log.info(f"RabbitMQ 消费者连接成功 - queue: {self.queue_name}")

        except Exception as e:
            log.error(f"RabbitMQ 消费者连接失败: {str(e)}")
            raise

    def start_consuming(self, callback):
        """
        开始消费消息

        Args:
            callback: 回调函数
        """
        try:
            def wrapper(ch, method, properties, body):
                try:
                    # 尝试 UTF-8 解码（Java 端已配置 Jackson2JsonMessageConverter）
                    message = json.loads(body.decode("utf-8"))
                    processed = callback(message)
                    if processed is False:
                        ch.basic_nack(delivery_tag=method.delivery_tag, requeue=True)
                        self.connection.sleep(1)
                    else:
                        ch.basic_ack(delivery_tag=method.delivery_tag)
                except (UnicodeDecodeError, json.JSONDecodeError) as e:
                    # 旧 Java 序列化消息直接丢弃，不再重试
                    log.warning(
                        f"跳过非 JSON 消息（旧 Java 序列化格式），queue={self.queue_name}, "
                        f"body_preview={body[:20].hex(' ')}..."
                    )
                    ch.basic_ack(delivery_tag=method.delivery_tag)
                except Exception as e:
                    log.error(f"处理消息失败: {str(e)}")
                    ch.basic_nack(delivery_tag=method.delivery_tag, requeue=True)
                    self.connection.sleep(1)

            self.channel.basic_qos(prefetch_count=1)
            self.channel.basic_consume(
                queue=self.queue_name,
                on_message_callback=wrapper
            )

            log.info(f"开始消费消息 - queue: {self.queue_name}")
            self.channel.start_consuming()

        except Exception as e:
            log.error(f"消费消息失败: {str(e)}")
            raise

    def close(self):
        """关闭连接"""
        if self.connection and not self.connection.is_closed:
            try:
                self.connection.add_callback_threadsafe(self.channel.stop_consuming)
            except Exception:
                pass

    def close_connection(self):
        """只由连接所属的消费线程调用。"""
        if self.connection and self.connection.is_open:
            self.connection.close()
