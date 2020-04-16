from flask import Flask
from flask import render_template
import dao, calculation
import pymysql, json

app = Flask(__name__)

@app.route('/')
def hello_world():
    return render_template('index.html')

@app.route('/<stock_name>')
# Get the input value as stock_name
def getResult(stock_name):
    # Spliting the string input into separate part to map the stocks
    stock_list = stock_name.split(",")
    obj = dao.dao()
    portfolio = {}
    print(stock_list)
    for item in stock_list:
        now_data = obj.listgetfunc(item)
        # get the stock volume
        now_v = obj.getfunc(item+'-v')
        # get the stock latest price
        now_p = obj.getfunc(item + "-p")
        portfolio[item] = [float(now_data[5]), int(now_v), float(now_p)]
    cal_obj = calculation.calculation()
    result = cal_obj.cal_portofilo(portfolio)
    print(result)
    index = obj.getfunc("index")

    return render_template('result.html', tech_index=float(index), portfolio=result, stock_name=stock_name)

if __name__ == '__main__':
    app.run(debug=True)
