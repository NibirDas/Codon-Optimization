from flask import Flask, request, jsonify, render_template
import csv

app = Flask(__name__)

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/upload', methods=['POST'])
def upload_file():
    file = request.files['file']
    file.save('codon.csv')

    # Read and print the contents of the CSV file
    with open('codon.csv', 'r') as file:
        csv_reader = csv.DictReader(file)
        codon_usage = [row for row in csv_reader]
        for row in codon_usage:
            print(row)

    return jsonify({'status': 'success', 'data': codon_usage})

if __name__ == '__main__':
    app.run(debug=True)

