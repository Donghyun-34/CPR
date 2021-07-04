from flask import Flask, jsonify, render_template, request

from flask_migrate import Migrate 
from flask_sqlalchemy import SQLAlchemy 

import sys

db = SQLAlchemy()
migrate = Migrate()

import config, models

app = Flask(__name__)

app.config.from_object(config)
    
#ORM
db.init_app(app)
migrate.init_app(app, db)


#route
@app.route('/')
def home():
    return jsonify(text='this is home')

@app.route('/checklist')
def checklist():
    return "this is checklist!"

@app.route("/map")
def map():
    return "this is map!"

@app.route("/admin")
def admin():
    return "this is admin!"

@app.route('/test')
def test():
    return render_template('post.html')

@app.route('/post', methods=['POST'])
def post():
    value = request.form['checklist']
    return value  


if __name__ == "__main__":
    app.run(host='0.0.0.0', port=int(sys.argv[1]), debug=True)
