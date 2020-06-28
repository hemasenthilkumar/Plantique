from flask import Flask, request, url_for, Flask,redirect
from flask_pymongo import PyMongo
from flask import Flask, render_template, request
from flask import request, abort, make_response
from base64 import b64encode
import os
import pymongo
import platform
import socket
import subprocess
import shlex
import re
import requests
from datetime import date
import datetime
import random, threading, webbrowser
from neo4j import GraphDatabase
import smtplib
import json
import jsonpickle

class JsonTransformer(object):
    def transform(self, myObject):
        return jsonpickle.encode(myObject, unpicklable=False)


g=GraphDatabase.driver(uri='bolt://localhost:11002',auth=("neo4j","hema13"))
session=g.session()


myclient = pymongo.MongoClient("mongodb://localhost:27017/")
mydb = myclient["testdb"]
mycol = mydb["GreenTreeOrders"]
mycol2=mydb["Chats"]
mycol3=mydb["profilepics"]

class Message:
  def __init__(self,name,msg):
    self.name=name
    self.msg=msg
class Product:
  def __init__(self, name, price,usname,id_=None,cat=None):
    self.name = name
    self.price = price
    self.username=usname
    self.id_ =id_ if id_ is not None else 0
    self.cat =cat if cat is not None else 0

class Product2:
  def __init__(self, name, price,id_=None):
    self.name = name
    self.price = price
    self.id_ =id_ if id_ is not None else 0

class Purchase:
     def __init__(self, name, price,usname):
         self.name = name
         self.price = price
         self.usname=usname
class Order:
  def __init__(self,list_P,total,date):
    self.list_P=list_P
    self.total=total
    self.date=date


app = Flask(__name__)
app.config['SECRET_KEY']='fshfhjdchbDAHC234GFGJHJ'


@app.route('/login')
def login_after():
    print("In login")
    session=g.session()
    s=''
    mes="Invalid credentials"
    us=request.args.get('us')
    ps=request.args.get('ps')
    q1="match(n:user) where n.username='"+us+"' and n.password='"+ps+"' return ID(n)"
    nodes=session.run(q1)
    for n in nodes:
        s=s+str(n["ID(n)"])
    if(s):
         return "Success!"
    else:
        status_code = flask. Response(status=401)
        return status_code

@app.route('/signup')
def signup():
    obj=[]
    s=request.args.get('info')
    print(obj)
    print(type(obj))
    s=s.replace('[','')
    s=s.replace(']','')
    obj=s.split(',')
    obj=[x.strip() for x in obj]
    us=obj[0]
    ps=obj[1]
    cps=obj[2]
    em=obj[3]
    bday=obj[4]
    up=obj[5]
    session=g.session()
    s=''
    regex = '^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$'
    
    print("bday is",bday)
    if bday!='':
        try:
            value= datetime.datetime.strptime(bday, '%d/%m/%Y').date()
        except ValueError:
            value=None
    else:
        value=None
    today=date.today()
    if(up=='0'):
        up='Home Based seller'
    elif(up=='1'):
        up='Whole saler'
    else:
        up='Just Gonna purchase'
    q0="match(n:user) where n.username='"+us+"' return ID(n)"
    nodes=session.run(q0)
    for n in nodes:
        s=s+str(n["ID(n)"])
    if (cps!=ps):
        message="Password doesnt match"
    elif (s):
        message="Already username exists!"
    elif(us==''):
        message="Username should not be empty"
    elif(ps==''):
        message="Password should not be empty"
    elif(cps==''):
        message="Confirm password should not be empty"
    elif(em==''):
        message="Email should not be empty"
    elif(bool(re.search(regex,em))==False):
        message="Invalid Email Id"
    elif value==None:
        message="Birth date should not be empty. Please provide in format (DD/MM/YYYY)"
    elif today<value:
        message="Date should not be in future!"
    else:
        q1="create(u:user{username:'" + us + "',bday:'"+bday+"',password:'" + ps + "',email:'" + em + "',role:'"+up+"',date_created:date()}) return u"
        nodes=session.run(q1)
        message="Sign up successful"
    return message


@app.route('/userhome')
def people():
    session=g.session()
    usname=request.args.get('usname')
    total={}
    p={}
    #q2="match(n:user) return n.username as users,n.role as role"
    q3=s="match(u:user{username:'"+usname+"'})-[f:follows]->(n:user) match (n:user)-[p:posted]->(z:post) return z.value,n.username,p.ontime"
    q4="match(u:user{username:'"+usname+"'})-[p:posted]->(p1:post) return u.username,p1.value,p.ontime"
    nodes3=session.run(q3)
    nodes4=session.run(q4)
    for n in nodes3:
        p.setdefault(n['n.username'], []).append([n['z.value'],n['p.ontime']])
    for n in nodes4:
        p.setdefault(n['u.username'], []).append([n['p1.value'],n['p.ontime']])
##    d={}
##    for k,v in p.items():
##        st=""
##        for i in v:
##            st+=' '.join(i)
##            st+=","
##        d[k]=st
    return json.dumps(p)

@app.route('/friends')
def friend():
    session=g.session()
    usname=request.args.get('usname')
    total={}
    follow=[]
    d={}
    p={}
    q1="match(u:user{username:'"+usname+"'})-[f:follows]->(d:user) return d.username as users"
    q2="match(n:user) return n.username as users,n.role as role"
    nodes=session.run(q1)
    nodes2=session.run(q2)
    role=0
    for n in nodes:
        follow.append(n['users'])
    for n in nodes2:
        total[n['users']]=n['role']
        role=n['role']
    print(total[usname])
    role=total[usname]
    if role=="Just Gonna purchase":
        role=0
    else:
        role=1
    del total[usname]
    for k,v in total.items():
        if k in follow:
            d[k]=1
        else:
            d[k]=0
    print(d)
    return json.dumps(d)


@app.route('/follow')
def follow():
    pep={}
    session=g.session()
    q0="match(u:user) return u.username as uname, u.role as role"
    nodes=session.run(q0)
    for n in nodes:
        pep[n["uname"]]=n["role"]
    usname  = request.args.get('usname', None)
    folname  = request.args.get('folname', None)
    value  = request.args.get('value', None)
    print(value)
    if(int(value)==0):
        l=[usname]
        q1="match(n:user{username:'"+usname+"'}),(u:user{username:'"+folname+"'}) create (n)-[f:follows{from:date()}]->(u)"
        session.run(q1)
    else:
        q2="match(u1:user{username:'"+usname+"'})-[f:follows]-(u2:user{username:'"+folname+"'}) delete f"
        session.run(q2)
    return "Success"
    
@app.route('/feeds')
def view_feeds():
    us=request.args.get('us')
    post=request.args.get('usp')
    x=datetime.datetime.now()
    q1="match(u:user{username:'"+us+"'}) create (p:post{value:'"+post+"'}) create (u)-[p1:posted{ontime:'"+x.strftime("%d-%b-%Y %I:%M:%S %p")+"'}]->(p)"
    session.run(q1)
    return "Sucess"


def cartString(d):
  s=""
  for k,v in d.items():
    s=s+" "+k+" X " + str(v)
  return s

@app.route('/Orders')
def orders():
  usname=request.args.get('usname')
  l=[]
  d={}
  i=0
  filt={"placedby":usname}
  for x in mycol.find(filt):
      print(x['products'])
      d=getDuplicatesWithCount(x['products'])
      print(d)
      p=cartString(d)
      od=x['orderDate']
      value= datetime.datetime.strptime(od, '%d-%b-%Y %I:%M:%S %p').date()
      o=Order(p,x['total'],value.strftime('%Y-%m-%d'))
      l.append(o)
  return json.dumps([ob.__dict__ for ob in l])

def getDuplicatesWithCount(listOfElems):
	dictOfElems = dict()
	for elem in listOfElems:
		if elem in dictOfElems:
			dictOfElems[elem] += 1
		else:
			dictOfElems[elem] = 1    
    # Returns a dict of duplicate elements and thier frequency count
	return dictOfElems


@app.route('/addProduct')
def addProduct1():
    loc=[]
    pnames=[]
    obj=[]
    c="Indoor"
    s=request.args.get('info')
    print(s)
    print(type(obj))
    s=s.replace('[','')
    s=s.replace(']','')
    obj=s.split(',')
    obj=[x.strip() for x in obj]
    print(obj)
    usname=obj[0]
    name=obj[1]
    price=obj[2]
    location=obj[3]
    category=obj[4]
    if(category=='0'):
        c="Indoor"
    if(category=='1'):
        c="Outdoor"
    if(category=='2'):
        c="Decorative"
    q4="match(n:user{username:'"+usname+"'})-[o:owns]-(p:product) return p.name"
    nodes2=session.run(q4)
    for n in nodes2:
      pnames.append(n['p.name'].lower())
    print(pnames)
    if name.lower() in pnames:
      mes="Already exists!"
      print(mes)
    else:
      q2="create(p:product{name:'"+name+"',price:'"+str(price)+"',category:'"+c+"',postedBy:'"+usname+"'}) return ID(p)"
      q3="match(p:product{name:'"+name+"'}),(n:user{username:'"+usname+"'}) create (n)-[o:owns]->(p)"
      print(q2)
      print(q3)
      nodes=session.run(q2)
      nodes1=session.run(q3)
      if(location=='1'):
          session.run("match(p:product{name:'"+name+"'}),(l:location{name:'Vellore'}) create (p)-[a:available_in]->(l)")
      if(location=='2'):
          session.run("match(p:product{name:'"+name+"'}),(l:location{name:'Chennai'}) create (p)-[a:available_in]->(l)")
      if(location=='3'):
          session.run("match(p:product{name:'"+name+"'}),(l:location{name:'Bangalore'}) create (p)-[a:available_in]->(l)")
      if(location=='4'):
          session.run("match(p:product{name:'"+name+"'}),(l:location{name:'Hyderabad'}) create (p)-[a:available_in]->(l)")
      mes="Successfully added product"
      print(mes)
    return mes

@app.route('/viewProduct')
def viewProduct():
  l=[]
  usname=request.args.get('usname')
  q1="match(u:user{username:'"+usname+"'})-[o:owns]->(p:product) return p.name,p.price,p.category,ID(p)"
  nodes=session.run(q1)
  for n in nodes:
    p=Product(n['p.name'],n['p.price'],usname,n['ID(p)'],n['p.category'])
    l.append(p)
  return json.dumps([ob.__dict__ for ob in l])


@app.route('/edit')
def edit():
    l=[]
    session=g.session()
    role=''
    date=''
    us=request.args.get('usname')
    q1="match(p:user{username:'"+us+"'}) return p.role as role, p.date_created as date"
    q2="MATCH (n:user{username:'"+us+"'})-[r]->() RETURN COUNT(r)"
    q3="MATCH (n:user{username:'"+us+"'})<-[r]-() RETURN COUNT(r)"
    nodes=session.run(q1)
    follow=session.run(q2)
    follow_by=session.run(q3)
    d1={}
    for c in follow:
        d1['Follows']=(c['COUNT(r)'])
    for f in follow_by:
        d1['Followed by']=(f['COUNT(r)'])
    for n in nodes:
        role=n['role']
        date=str(n['date'])
    d1['Role']=role
    d1['Date']=date
    print(d1)
    return json.dumps(d1)


@app.route('/edit-rs')
def edit_rs():
    session=g.session()
    us=request.args.get('us')
    usp=request.args.get('usp')
    q1="match(n:user{username:'"+us+"'}) set n.role='"+usp+"'"
    session.run(q1)
    return "Success"


@app.route('/showCart')
def showCart():
  l=[]
  total=0
  usname=request.args.get('usname')
  q6="match(u:user{username:'"+usname+"'})-[h:has]-(c:cart)-[c1:contains]-(p:product) return p.name,p.price,ID(c1)"
  nodes=session.run(q6)
  for n in nodes:
    total=total+int(n['p.price'])
    p=Product2(n['p.name'],n['p.price'],n['ID(c1)'])
    l.append(p)
  return json.dumps([ob.__dict__ for ob in l])

def insert_mongo(usname,tot):
  l=[]
  q6="match(u:user{username:'"+usname+"'})-[h:has]-(c:cart)-[c1:contains]-(p:product) return p.name,p.price,ID(c1)"
  nodes=session.run(q6)
  for n in nodes:
    l.append(n['p.name'])
  x=datetime.datetime.now()
  mycol.insert({"placedby":usname,"products":l,"total":tot,"orderDate":x.strftime("%d-%b-%Y %I:%M:%S %p")})

@app.route('/Emptycart')
def emptycart():
  usname=request.args.get('usname')
  total=request.args.get('tot')
  insert_mongo(usname,total)
  q7="match(u:user{username:'"+usname+"'})-[h:has]-(c:cart)-[c1:contains]-(p:product) delete c1"
  session.run(q7)
  return "Success"

@app.route('/purchase')
def purchase():
    l=[]
    usname=request.args.get('usname')
    q5="match(u:user{username:'"+usname+"'})-[f:follows]->(n:user) match(n:user)-[o:owns]->(p:product) return n.username,p.price,p.name"
    nodes=session.run(q5)
    for n in nodes:
        p=Purchase(n['p.name'],n['p.price'],n['n.username'])
        l.append(p)
    return json.dumps([ob.__dict__ for ob in l])


def checkCart(usname):
  q4="match(n:user{username:'"+usname+"'})-[h:has]->(c:cart) return ID(c)"
  nodes=session.run(q4)
  if(nodes.peek()):
    for n in nodes:
      var=n['ID(c)']
  else:
    var="None"
  return var
  

@app.route('/cart')
def cart():
    usname=request.args.get('usname')
    product=request.args.get('product')
    cart_id=checkCart(usname)
    if cart_id!="None":
      q3="match (u:user{username:'"+usname+"'})-[h:has]->(c:cart), (p:product{name:'"+product+"'}) create (c)-[c1:contains]->(p)"
      nodes=session.run(q3)  
    else:
      #create cart
      q5="match(u:user{username:'"+usname+"'}) create (u)-[h:has]->(c:cart)"
      q3="match (u:user{username:'"+usname+"'})-[h:has]->(c:cart), (p:product{name:'"+product+"'}) create (c)-[c1:contains]->(p)"
      nodes=session.run(q5)
      session.run(q3)
    return "Sucess"

@app.route('/removeProduct')
def removeProduct():
  pid=request.args.get('pid')
  usname=request.args.get('usname')
  q8="match()-[c:contains]-() where ID(c)="+pid+" delete c"
  session.run(q8)
  return "sucess"

@app.route('/setURI')
def seturi():
    uri=request.args.get('uri')
    usname=request.args.get('usname')
    if uri:
        #mycol3.insert({"usname":usname,"uri":uri})
        mycol3.replace_one({"usname":usname},{"usname":usname,"uri":uri},True)
    return "Sucess"

@app.route('/getURI')
def geturi():
    usname=request.args.get('usname')
    for x in mycol3.find({"usname":usname}):
        val= x["uri"]
    return val
    


if __name__=='__main__':
    app.run(debug=True,host='0.0.0.0')
