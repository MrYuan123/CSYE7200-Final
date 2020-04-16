class calculation(object):
    def __init__(self):
        pass

    def cal_portofilo(self, data):
        sum = 0
        totaltrans = 0
        for key in data:
            nowtrans = data[key][1]*data[key][2]
            sum += data[key][0]*nowtrans
            totaltrans += nowtrans

        return sum/totaltrans
# Test data
# if __name__ == "__main__":
#     obj = calculation()
#     #data format:
#     # [value of risk, average-volume, latest-price]
#     # how to calculate the technical index: data store all stocks
#     # how to calculate the protfolio of stocks, input the required sotcks you want to calculate
#     data = {"appl":[0.12222, 1, 1], "goog": [0.9999, 1, 1]}
#     result = obj.cal_portofilo(data)
#     print(result)