import random
from flask import Flask, render_template, request

app = Flask(__name__)



@app.route('/', methods=['GET', 'POST'])
def index():
    optimized_sequence = None
    optimized_cai = None
    default_cai_range = (0, 1)

    if request.method == 'POST':
        sequence = request.form['sequence']
        organism = request.form['organism']

        # Get organism data from the dictionary
        organism_info = organism_data.get(organism, {})
        codon_usage = organism_info.get('codon_usage', {})
        target_cai = organism_info.get('target_cai', 0.0)
        default_cai_range=organism_info.get('default_cai_range',(0,1))
        optimized_sequence = optimize_codon_sequence(target_cai, sequence, codon_usage)
        optimized_cai = calculate_cai(optimized_sequence, codon_usage)

    return render_template('index.html', optimized_sequence=optimized_sequence, optimized_cai=optimized_cai, default_target_cai=default_cai_range)

if __name__ == '__main__':
    app.run(debug=True)
