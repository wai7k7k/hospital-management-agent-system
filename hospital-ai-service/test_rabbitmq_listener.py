import pika
import json
from datetime import datetime
from app.utils.config import settings

def test_rabbitmq_listener():
    try:
        print(f"[{datetime.now()}] 正在连接 RabbitMQ...")
        print(f"[{datetime.now()}] 主机: {settings.RABBITMQ_HOST}:{settings.RABBITMQ_PORT}")
        print(f"[{datetime.now()}] 用户: {settings.RABBITMQ_USER}")

        credentials = pika.PlainCredentials(
            settings.RABBITMQ_USER,
            settings.RABBITMQ_PASSWORD
        )

        connection = pika.BlockingConnection(
            pika.ConnectionParameters(
                host=settings.RABBITMQ_HOST,
                port=settings.RABBITMQ_PORT,
                virtual_host=settings.RABBITMQ_VHOST,
                credentials=credentials,
                heartbeat=600,
                blocked_connection_timeout=300
            )
        )

        channel = connection.channel()

        print(f"[{datetime.now()}] ✅ RabbitMQ 连接成功！")

        channel.exchange_declare(
            exchange='hospital.events',
            exchange_type='topic'
        )

        channel.queue_declare(queue='register.created.queue')

        channel.queue_bind(
            exchange='hospital.events',
            queue='register.created.queue',
            routing_key='register.created'
        )

        print(f"[{datetime.now()}] 开始监听挂号创建事件...")
        print("按 Ctrl+C 停止监听")
        print("-" * 80)

        def callback(ch, method, properties, body):
            try:
                message = json.loads(body)
                print(f"\n[{datetime.now()}] 📨 收到挂号创建事件:")
                print(f"  事件类型: {message.get('eventType')}")
                print(f"  挂号ID: {message.get('registerId')}")
                print(f"  患者ID: {message.get('patientId')}")
                print(f"  科室: {message.get('depName')}")
                print(f"  挂号时间: {message.get('registerDate')}")
                print(f"  完整消息: {json.dumps(message, indent=2, ensure_ascii=False)}")
                print("-" * 80)

                ch.basic_ack(delivery_tag=method.delivery_tag)
            except Exception as e:
                print(f"解析消息失败: {e}")
                print(f"原始消息: {body}")

        channel.basic_consume(
            queue='register.created.queue',
            on_message_callback=callback,
            auto_ack=False
        )

        try:
            channel.start_consuming()
        except KeyboardInterrupt:
            print("\n停止监听")
            channel.stop_consuming()

        connection.close()

    except pika.exceptions.AMQPConnectionError as e:
        print(f"[{datetime.now()}] ❌ 无法连接到 RabbitMQ")
        print(f"[{datetime.now()}] 错误: {e}")
        print(f"[{datetime.now()}] 请检查:")
        print(f"  1. RabbitMQ 服务器 {settings.RABBITMQ_HOST} 是否运行")
        print(f"  2. 网络连接是否正常")
        print(f"  3. 用户名和密码是否正确")
        print(f"  4. 防火墙是否允许连接")
    except Exception as e:
        print(f"[{datetime.now()}] ❌ 监听失败: {e}")

if __name__ == '__main__':
    test_rabbitmq_listener()
