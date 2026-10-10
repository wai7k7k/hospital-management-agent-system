import pika
import sys
from datetime import datetime
from app.utils.config import settings

def check_rabbitmq_connection():
    try:
        print(f"[{datetime.now()}] 正在连接 RabbitMQ...")
        print(f"[{datetime.now()}] 主机: {settings.RABBITMQ_HOST}:{settings.RABBITMQ_PORT}")
        print(f"[{datetime.now()}] 用户: {settings.RABBITMQ_USER}")
        print(f"[{datetime.now()}] 虚拟主机: {settings.RABBITMQ_VHOST}")

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
        print(f"[{datetime.now()}] ✅ 交换机 'hospital.events' 已声明")

        queues = [
            'patient.symptom.queue',
            'register.created.queue',
            'inspect.created.queue',
            'report.completed.queue',
            'prescription.created.queue'
        ]

        for queue_name in queues:
            result = channel.queue_declare(queue=queue_name)
            message_count = result.method.message_count
            print(f"[{datetime.now()}] ✅ 队列 '{queue_name}' 已声明，消息数: {message_count}")

        bindings = [
            ('patient.symptom.queue', 'patient.symptom'),
            ('register.created.queue', 'register.created'),
            ('inspect.created.queue', 'inspect.created'),
            ('report.completed.queue', 'report.completed'),
            ('prescription.created.queue', 'prescription.created')
        ]

        for queue_name, routing_key in bindings:
            channel.queue_bind(
                exchange='hospital.events',
                queue=queue_name,
                routing_key=routing_key
            )
            print(f"[{datetime.now()}] ✅ 绑定已创建: {queue_name} ← {routing_key}")

        connection.close()
        print(f"\n[{datetime.now()}] ✅ RabbitMQ 检查完成！")

        return True

    except pika.exceptions.AMQPConnectionError as e:
        print(f"[{datetime.now()}] ❌ 无法连接到 RabbitMQ")
        print(f"[{datetime.now()}] 错误: {e}")
        print(f"[{datetime.now()}] 请检查:")
        print(f"  1. RabbitMQ 服务器 {settings.RABBITMQ_HOST} 是否运行")
        print(f"  2. 网络连接是否正常")
        print(f"  3. 用户名和密码是否正确")
        print(f"  4. 防火墙是否允许连接")
        return False
    except Exception as e:
        print(f"[{datetime.now()}] ❌ 检查失败: {e}")
        return False

if __name__ == '__main__':
    success = check_rabbitmq_connection()
    sys.exit(0 if success else 1)
