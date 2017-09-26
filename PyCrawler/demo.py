# -*- coding:utf-8

import requests
import json
import hashlib
default_timeout = 10

class NetEase:
    def __init__(self):
        self.header = {
            'Accept':"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
            'Accept-Encoding':'gzip, deflate, br',
            'Accept-Language':'zh-CN,zh;q=0.8,en;q=0.6',
            'Connection':'keep-alive',
            'Content-Type':'application/x-www-form-urlencoded',
            'Host':'',
            'Referer':'',
            'User-Agent':'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36'
        }
        self.cookie = {
        }
    def httpRequest(self, method, action, query=None, urlencoded=None, callback=None, timeout=None):
        if (method == 'GET'):
            connection = requests.get(action, headers=self.header, timeout=default_timeout, params=query)
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
    def login(self, username, password):
        action = 'https://passport.58.com/login/pc/dologin'
        data = {
            'username': username,
            'password': hashlib.md5(password).hexdigest(),
            'isremember': 'false',
            'source':'passport'
        }
        try:
            return self.httpRequest('POST', action, data)
        except Exception,e:
            return {'code': 501}
    def doGet(self,url,params=None,host=None):
        self.header['Host'] = host
        try:
            return self.httpRequest('GET',url,params)
        except Exception,e:
            return {'code':501}


Login = NetEase()
url = 'http://www.zhihu.com'
host = 'www.zhihu.com'
params = ''
data = Login.doGet(url,params,host)
print ('data:' + data)
# data = Login.login('13572171443','wks1133')
# if data['code']==200:
#     print('登录成功， UserId：'+str(data['account']['id']))
# else:
#     print('登录错误，错误Code：'+str(data['code']))




