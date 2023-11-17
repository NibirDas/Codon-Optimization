import random
from flask import Flask, render_template, request

app = Flask(__name__)

organism_data = {
    'custom': {
        'codon_usage': {
            # Add your custom codon usage here
        },
        'target_cai': 0.9,
        'default_cai_range': (0.8, 0.9)
    },
    'human': {
        #Homo Sapiens
	'codon_usage': {
        'UUU':17.6, 'UCU':15.2,'UAU':12.2, 'UGU':10.6,
        'UUC':20.3, 'UCC':17.7, 'UAC':15.3, 'UGC':12.6,
        'UUA':7.7, 'UCA':12.2, 'UAA':1.0, 'UGA':1.6,
        'UUG':12.9, 'UCG':4.4, 'UAG':0.8, 'UGG':13.2,
        
        'CUU':13.2, 'CCU':17.5, 'CAU':10.9, 'CGU':4.5,
        'CUC':19.6, 'CCC':19.8, 'CAC':15.1, 'CGC':10.4,
        'CUA':7.2, 'CCA':16.9, 'CAA':12.3, 'CGA':6.2,
        'CUG':39.6, 'CCG':6.9, 'CAG':34.2, 'CGG':11.4,
        
        'AUU':16.0, 'ACU':13.1, 'AAU':17.0, 'AGU':12.1,
        'AUC':20.8, 'ACC':18.9, 'AAC':19.1, 'AGC':19.5,
        'AUA':7.5, 'ACA':15.1, 'AAA':24.4, 'AGA':12.2,
        'AUG':22.0, 'ACG':6.1, 'AAG':31.9, 'AGG':12.0,
        
        'GUU':11.0, 'GCU':18.4, 'GAU':21.8, 'GGU':10.8,
        'GUC':14.5, 'GCC':27.7, 'GAC':25.1, 'GGC':22.2,
        'GUA':7.1, 'GCA':15.8, 'GAA':29.0, 'GGA':16.5,
        'GUG':28.1, 'GCG':7.4, 'GAG':39.6, 'GGG':16.5,},

	'target_cai': 0.9,
	'default_cai_range': (0.8, 1.0)
    },
    'Lactobacillus pentosus': {
	'codon_usage': {
        'UUU':12.4, 'UCU':6.2, 'UAU':14.6, 'UGU':0.7,
        'UUC':22.9, 'UCC':5.1, 'UAC':23.3, 'UGC':0.4,
        'UUA':25.1, 'UCA':21.5, 'UAA':1.8, 'UGA':0.0,
        'UUG':25.1, 'UCG':3.6, 'UAG':0.4, 'UGG':12.4,
        
        'CUU':9.1, 'CCU':10.9, 'CAU':9.5, 'CGU':16.4,
        'CUC':8.0, 'CCC':1.1, 'CAC':15.3, 'CGC':5.5,
        'CUA':2.9, 'CCA':26.6, 'CAA':32.4, 'CGA':1.1,
        'CUG':6.5, 'CCG':4.4, 'CAG':5.1, 'CGG':16.4,
        
        'AUU':30.2, 'ACU':13.1, 'AAU':8.0, 'AGU':8.0,
        'AUC':32.0, 'ACC':13.8, 'AAC':32.7, 'AGC':5.8,
        'AUA':0.0, 'ACA':6.2, 'AAA':20.7, 'AGA':0.0,
        'AUG':30.2, 'ACG':22.9, 'AAG':6.9, 'AGG':0.0,
        
        'GUU':37.5, 'GCU':29.5, 'GAU':46.2, 'GGU':34.6,
        'GUC':20.4, 'GCC':28.7, 'GAC':33.1, 'GGC':24.0,
        'GUA':3.3, 'GCA':21.8, 'GAA':61.1, 'GGA':4.7,
        'GUG':10.5, 'GCG':14.6, 'GAG':1.8, 'GGG':11.3,},
	
	'target_cai': 0.6,
	'default_cai_range': (0.5, 0.7)
    }
}

def calculate_cai(sequence, codon_usage):
    codon_counts = {} 
    total_codons = 0
    for i in range(0, len(sequence), 3):
        codon = sequence[i:i + 3] # CGT - 
        codon_counts[codon] = codon_counts.get(codon, 0) + 1
        
        total_codons += 1

    #print (codon_counts)
    cai = 1.0

    for codon, count in codon_counts.items():
        if codon in codon_usage:
            cai *= (codon_usage[codon] ** count)

    cai = cai ** (1 / total_codons)
    return cai

def optimize_codon_sequence(target_cai, current_sequence, codon_usage, max_iterations=1000, mutation_rate=0.1):
    current_cai = calculate_cai(current_sequence, codon_usage)
    best_sequence = current_sequence
    best_cai = current_cai

    for _ in range(max_iterations):
        position_to_mutate = random.randint(0, len(current_sequence) - 3)
        new_sequence = list(current_sequence)
        new_codon = random.choice(list(codon_usage.keys()))
        new_sequence[position_to_mutate:position_to_mutate+3] = list(new_codon)
        new_sequence = ''.join(new_sequence)
        
        new_cai = calculate_cai(new_sequence, codon_usage)

        if abs(new_cai - target_cai) < abs(best_cai - target_cai):
            best_sequence = new_sequence
            best_cai = new_cai

        current_sequence = best_sequence
        current_cai = best_cai

    return best_sequence

@app.route('/', methods=['GET', 'POST'])
def index():
    optimized_sequence = None
    optimized_cai = None

    if request.method == 'POST':
        sequence = request.form['sequence']
        organism = request.form['organism']

        # Get organism data from the dictionary
        organism_info = organism_data.get(organism, {})
        codon_usage = organism_info.get('codon_usage', {})
        target_cai = organism_info.get('target_cai', 0.9)
        default_cai_range=organism_info.get('default_cai_range',(0,1))

        optimized_sequence = optimize_codon_sequence(target_cai, sequence, codon_usage)
        optimized_cai = calculate_cai(optimized_sequence, codon_usage)

    return render_template('index.html', optimized_sequence=optimized_sequence, optimized_cai=optimized_cai, default_target_cai=default_cai_range)

if __name__ == '__main__':
    app.run(debug=True)
