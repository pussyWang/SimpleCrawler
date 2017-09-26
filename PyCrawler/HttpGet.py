# -*- coding:utf-8

import json
import requests

default_timeout = 10

class NetAccess:
    def httpRequest(self, method, action, query=None, urlencoded=None, callback=None, timeout=None):
        if (method == 'GET'):
            connection = requests.get(action,timeout=default_timeout, params=query)
        elif (method == 'POST'):
            connection = requests.post(
                action,
                data=query,
                headers=self.header,
                timeout=default_timeout
            )
        connection.encoding = "UTF-8"
        connection = json.loads(connection.text)
        return connection

    def doGet(self,url,params=None):
        action = url
        data = params
        try:
            return self.httpRequest('GET', action, data)
        except Exception,e:
            return {'code': 501}


# url = 'http://biandang.58.com/demo'
# params = {
#     'name':'wangkang',
#     'age':'18'
# }
Login = NetAccess()
# data = Login.doGet(url,params)
# print ('msg:' + str(data['msg']))
# print ('name:' + str(data['result']['name']))
# print ('age:' + str(data['result']['age']))
# print ('status:'+str(data['status']))
url = 'http://zhihu.com'
data = Login.doGet(url)
print ('data:' + data)
# print ('data:' + str(data))
# if data['code']==200:
#     print('data：'+str(data))
# else:
#     print('错误Code：'+str(data))