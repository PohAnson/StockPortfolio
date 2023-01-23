from flask import Flask

from server.apis import api_bp

app = Flask(__name__)


app.register_blueprint(api_bp)
print(app.url_map)
app.run(debug=True)
