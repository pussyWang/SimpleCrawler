# -*- coding:utf-8
import json
import requests

default_timeout = 10

class LoginDemo:
    def __init__(self):
        self.header = {
            'Accept': "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
            'Accept-Encoding': 'gzip, deflate, br',
            'Accept-Language': 'zh-CN,zh;q=0.8,en;q=0.6',
            'Connection': 'keep-alive',
            'Content-Type': 'application/x-www-form-urlencoded',
            'Host': 'passport.58.com',
            'Referer': 'https://passport.58.com/login/?path=http%3A//bj.58.com/'
                       '&PGTID=0d100000-0000-170a-7389-fd3af9e87693&ClickID=1',
            'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/'
                          '537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36',
            'Origin': 'https://passport.58.com',
            'Upgrade-Insecure-Requests': '1'
        }
        self.cookie = {}
    def httpPost(self, action, query=None, urlencoded=None, callback=None, timeout=None):
        connection = requests.post(
            action,
            data=query,
            headers=self.header,
            timeout=default_timeout
        )
        connection.encoding = "UTF-8"
        connection = json.loads(connection.text)
        return connection
    def login(self,url,query,cookie):
        action = url
        data = query
        self.cookie = cookie
        try:
            return self.httpPost('POST', action, data)
        except Exception,e:
            return {'code': 501}

url = 'https://passport.58.com/login/pc/dologin'
params = {
    'username': '13572171443',
    'password': '6288ee450ac62bab9f703313c985c669eefbd6c9b6ae9869fbb3bbfc097da2a70a'
                '05a769bfaabf3b102bb67205a02c0dad077c0beebc84dba3335eb1aa3ef9aba1d32'
                '7e37a3904f81443287da34a7dc75329a26e39afe5e5e76c735275240b7d338bc5d8'
                '8dcd3278079a02308c9a8c830d6ab15a832408e411c67b0f20e794db',
    'isremember': 'false',
    'source': 'passport',
    'finger2': 'zh-CN|24|1|4|1366_768|1366_728|-480|1|1|1|undefined|1|unknown|Win32|unknown|5|'
               'false|false|false|false|false|0_false_false|d41d8cd98f00b204e9800998ecf8427e|'
               '12e0032953782ce657bdda7808b9bda1',
    'fingerprint': 'B031979AE328B24F5279C54E0ABF23A87070DA5638916BAC_111'
}
cookie = {
    'TDC_token':'737962951',
    'id58':'05cDHlZO6Udiwmk9DFbYAg==',
    'bj58_id58s':'MER2OFI5cl9tQ1hrNjk5OA==',
    'als':'0',
    'gr_user_id':'48352a6c-15e5-40cd-90d9-4c1a784b3b34',
    'wmda_uuid':'5b510af41e7072570ffd0450e48164ad',
    'wmda_new_uuid':'1',
    'myfeet_tooltip':'end',
    'cookieuid':'0194d431-298c-4052-a862-0ced9b55fabd',
    'commonTopbar_myfeet_tooltip':'end',
    'commontopbar_myfeet_tooltip':'end',
    'wmda_visited_projects':'%3B1732030748417%3B1732029604225%3B1732032267521%3B1409632296065',
    'Hm_lvt_3bb04d7a4ca3846dcc66a99c3e861511':'1505374248,1505390780',
    'Hm_lvt_e15962162366a86a6229038443847be7':'1505374248,1505390781',
    'GA_GTID':'0d3037c3-0000-1c7d-375d-a12c4a61a014',
    '_ga':'GA1.2.1307693615.1505196486',
    'mcity':'bj',
    '__utma':'253535702.1307693615.1505196486.1505704231.1505823038.7',
    '__utmz':'253535702.1505823038.7.7.utmcsr=biandang.58.com|utmccn=(referral)|utmcmd=referral|utmcct=/channel/6310/bj',
    '_bu':'2016070614282473723cf9',
    'logintab_new':'1',
    'pptmobile':'13572171443',
    'firstLogin':'true',
    'ipcity':'bj%7C%u5317%u4EAC%7C0',
    'pptuname':'13572171443',
    'xxzl_smartid':'da817b0efcdbe032221a9e91612bea40',
    'city':'bj',
    '58home':'bj',
    'commontopbar_city':'1%7C%u5317%u4EAC%7Cbj',
    '58tj_uuid':'bccd4f7e-a311-43f1-b898-a01607d6197b',
    'new_session':'0',
    'new_uv':'62',
    'utm_source':'',
    'spm':'',
    'init_refer':'',
    'wxtk':'8815DD9B0985DB165908B90F691D4B07',
    'wxtksec':'vcoavuz1506330112123',
    'xxzl_deviceid':'QPQ9DnrtZf0LuWp8LlZ5d1FDlZGq1y6RZmvfJx9MK1vfxceh2GbvYR6dDcWhioAm',
    'ppStore_fingerprint':'B031979AE328B24F5279C54E0ABF23A87070DA5638916BAC%EF%BC%BF1506330161455'
}
Login = LoginDemo()
data = Login.login(url,params,cookie)
print 'result = ' + str(data)
