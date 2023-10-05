import socketio
import json
import time


class CustomSocketIOClient:
    def __init__(self, server_url):
        self.sio = socketio.Client()
        self.server_url = server_url

        self.mock_table = {
            "1": "Table1", "2": "Table2", "3": "Table3"
        }

        self.setup_events()

    def setup_events(self):
        @self.sio.event
        def connect():
            print("Connected to Server")

        @self.sio.event
        def response(arg):
            data = arg
            print(f"Response := {data}")

        @self.sio.event
        def disconnect():
            print('Disconnected from server')

        @self.sio.event
        def receiver_goto_dest(data):
            dest: str = data
            print(f'Go to {dest}')
            for i in range(3):
                message = "complete" if i == 2 else f"Waiting.... {i}"
                time.sleep(1)
                self.sio.emit("receiver_moving_status", message)

        @self.sio.event
        def sender_location():
            time.sleep(1)
            self.sio.emit("receiver_location",
                          data=json.dumps(self.mock_table))
            # time.sleep(1)
            # self.sio.emit("move_to")

    def start(self):
        self.sio.connect(self.server_url)
        self.sio.wait()


if __name__ == "__main__":
    server_url = "http://10.59.18.213:5000"
    custom_socket_io = CustomSocketIOClient(server_url)
    custom_socket_io.start()
