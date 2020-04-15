from flask import Flask
from flask import render_template
import pymysql, json

# cache = Cache()
#
# config = {
#   'CACHE_TYPE': 'redis',
#   'CACHE_REDIS_HOST': '127.0.0.1',
#   'CACHE_REDIS_PORT': 6379,
#   'CACHE_REDIS_DB': '',
#   'CACHE_REDIS_PASSWORD': ''
# }

app = Flask(__name__)

app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = True
app.config['SQLALCHEMY_DATABASE_URI'] = (
    'mysql://admin:gc995255@127.0.0.1:3306/moviedb'
)

app.config['SQLALCHEMY_ECHO'] = True

# app.config.from_object(config)
# cache.init_app(app)

import redis
redisobj = redis.Redis(host='127.0.0.1', port = 6379)

@app.route('/')
def hello_world():
    return render_template('index.html')


@app.route('/<stock_name>')
def getResult(stock_name):
    print(stock_name)
    stock_dict = stock_name.split(",")
    result = []
    for item in stock_dict:
        try:
            # conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', passwd='gc995255', db='moviedb',
            #                        charset='utf8')
            # cursor = conn.cursor()
            # sql_query = "select Value from Stock where Name = \"%s\"" % stock_name
            # print(sql_query)
            # rvalue = cursor.execute(sql_query)
            # print(rvalue)
            # cursor.fetchall()
            # conn.close()
            rvalue = redisobj.get(item)
            result.append(rvalue)
        except:
            print("Error!")
    # 执行SQL语句

    return render_template('index.html', result=result)


if __name__ == '__main__':
    app.run(debug=True)
