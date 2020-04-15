import redis
class dao(object):
    host = "localhost"
    port = "6379"
    connection = None

    def __init__(self):
        self.connection = self.connectfunc()

    def connectfunc(self):
        return redis.Redis(host=self.host, port=self.port)

    def getfunc(self, name):
        return self.connection.get(name)

    def listgetfunc(self, name):
        return self.connection.lrange(name, 0, 5)