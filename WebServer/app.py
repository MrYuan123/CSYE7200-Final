from flask import Flask
from flask import render_template
import dao, calculation
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

# app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = True
# app.config['SQLALCHEMY_DATABASE_URI'] = (
#     'mysql://admin:gc995255@127.0.0.1:3306/moviedb'
# )
#
# app.config['SQLALCHEMY_ECHO'] = True

# app.config.from_object(config)
# cache.init_app(app)

# import redis
# redisobj = redis.Redis(host='127.0.0.1', port = 6379)

@app.route('/')
def hello_world():
    return render_template('index.html')

@app.route('/<stock_name>')
def getResult(stock_name):
    stock_list = stock_name.split(",")
    obj = dao.dao()
    portfolio = {}
    print(stock_list)
    for item in stock_list:
        now_data = obj.listgetfunc(item)
        now_v = obj.getfunc(item+'-v')
        now_p = obj.getfunc(item + "-p")
        portfolio[item] = [float(now_data[5]), int(now_v), float(now_p)]
    cal_obj = calculation.calculation()
    result = cal_obj.cal_portofilo(portfolio)
    print(result)
    index = obj.getfunc("index")

    # volume = float(redisobj.get("appl-v"))
    #int.from_bytes(testBytes, byteorder='big')
    # int_res = [float(x) for x in res]
    # result = []
    # for item in stock_dict:
    #     try:
    #         rvalue = redisobj.get(item)
    #         result.append(rvalue)
    #     except:
    #         print("Error!")
    # 执行SQL语句
    # print(int_res)
    return render_template('result.html', tech_index=float(index), portfolio=result, stock_name=stock_name)


if __name__ == '__main__':
    app.run(debug=True)
