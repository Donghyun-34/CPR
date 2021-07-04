import sqlite3

f = open("C:\\Users\\82102\\Desktop\\CPR_re\\data_input\\sickness.txt", 'r', encoding='utf-8')
data = f.read()
f_con = data.split('b')
s_con = []
for i in f_con:
    s_con += i.split('a')
f.close()

conn = sqlite3.connect("../cpr.db")
cur = conn.cursor()

for i in s_con:
    cur.execute("INSERT INTO sickness(sick) VALUES(?);", i[0])
    cur.execute("SELECT id FROM sickness WHERE sick = ?;", i[0])
    id = cur.fetchall()
    cur.execute("INSERT INTO sick_cha(sick_id, cha, symp, cop) VALUES(?,?,?,?);", (id[0], i[1], i[2], i[3]))
    
conn.commit()
conn.close()