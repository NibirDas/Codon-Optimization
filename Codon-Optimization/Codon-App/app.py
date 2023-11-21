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
    'pentus': {
        #Lactobasillus Pentosus
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

    },
    'mushroom': {
        #Agaricus bisporus
	'codon_usage': {
        'UUU':14.0, 'UCU':17.8, 'UAU':13.0, 'UGU':6.8,
        'UUC':27.4, 'UCC':14.3, 'UAC':16.1, 'UGC':9.3,
        'UUA': 4.4, 'UCA':9.8, 'UAA':1.1, 'UGA':1.0,
        'UUG':15.2, 'UCG':10.5, 'UAG':0.8, 'UGG':14.8,

        'CUU':19.8, 'CCU': 19.3, 'CAU':12.1, 'CGU':11.2,
        'CUC':26.6, 'CCC':16.5, 'CAC':11.1, 'CGC':9.2,
        'CUA':5.8, 'CCA':9.0, 'CAA':20.5, 'CGA':6.3,
        'CUG':9.5, 'CCG':8.0, 'CAG':14.5, 'CGG':4.0,

        'AUU':22.9, 'ACU':21.0, 'AAU':21.3, 'AGU':10.3,
        'AUC':28.4, 'ACC':21.7, 'AAC':28.1, 'AGC':11.2,
        'AUA':5.6, 'ACA':11.5, 'AAA':21.0, 'AGA':5.1,
        'AUG':18.4, 'ACG':9.2, 'AAG':26.9, 'AGG':6.9,

        'GUU':23.6, 'GCU':32.2, 'GAU':30.5, 'GGU':32.4,
        'GUC':31.0, 'GCC':25.7, 'GAC':25.0, 'GGC':26.4,
        'GUA':7.3, 'GCA':16.8, 'GAA':26.5, 'GGA':21.5,
        'GUG':9.7, 'GCG':11.6, 'GAG':22.7, 'GGG':8.1},
	
	'target_cai': 0.6,
	'default_cai_range': (0.5, 0.7)

    },
    'gorilla': {
        #Gorilla Gorilla
	'codon_usage': {
        'UUU':17.5, 'UCU':16.2, 'UAU':14.0, 'UGU':11.2,
        'UUC':23.9, 'UCC':17.0,  'UAC':17.0, 'UGC':13.5,
        'UUA':7.6, 'UCA':11.2,  'UAA':0.9,  'UGA':1.8,
        'UUG':13.2, 'UCG':3.4,  'UAG':0.7,  'UGG':15.6,

        'CUU': 13.2, 'CCU':15.8, 'CAU':11.9, 'CGU':4.8,
        'CUC':22.3,  'CCC':18.8, 'CAC':16.2, 'CGC':10.9,
        'CUA': 7.8,  'CCA':14.4, 'CAA':14.5, 'CGA': 5.7,
        'CUG':43.2,  'CCG': 6.5, 'CAG':37.5, 'CGG': 9.7,

        'AUU':16.9,  'ACU':13.5, 'AAU':16.1, 'AGU':10.6,
        'AUC':22.9,  'ACC':20.8, 'AAC':18.7, 'AGC':17.5,
        'AUA': 8.9,  'ACA':15.3, 'AAA':23.6, 'AGA':14.9,
        'AUG':22.9,  'ACG': 5.8, 'AAG':29.3, 'AGG':12.9,

        'GUU':12.1,  'GCU':18.9, 'GAU':16.6, 'GGU': 9.4,
        'GUC':15.2,  'GCC':26.8, 'GAC':22.2, 'GGC':19.4,
        'GUA': 6.9,  'GCA':14.7, 'GAA':22.9, 'GGA':15.2,
        'GUG':29.1,  'GCG': 7.6, 'GAG':36.9, 'GGG':17.4,},
	
	'target_cai': 0.5,
	'default_cai_range': (0.4, 0.6)

    }    
}

# Function to calculate the Codon Adaptation Index (CAI) for a given sequence and codon usage
def calculate_cai(sequence, codon_usage):
    codon_counts = {} 
    total_codons = 0
	# Counting occurrences of each codon in the sequence
    for i in range(0, len(sequence), 3):
        codon = sequence[i:i + 3] # CGT - 
        codon_counts[codon] = codon_counts.get(codon, 0) + 1   
        total_codons += 1

    cai = 1.0

	# Calculating CAI based on the product of codon frequencies
    for codon, count in codon_counts.items():
        if codon in codon_usage:
            cai *= (codon_usage[codon] ** count)
	# Taking the geometric mean to get the final CAI
    cai = cai ** (1 / total_codons)
    return cai

# Function to optimize a given sequence to match the target CAI using random mutations
def optimize_codon_sequence(target_cai, current_sequence, codon_usage, max_iterations=1000, mutation_rate=0.1):
    current_cai = calculate_cai(current_sequence, codon_usage)
    best_sequence = current_sequence
    best_cai = current_cai

    for _ in range(max_iterations):
	    # Randomly selecting a position to mutate
        position_to_mutate = random.randint(0, len(current_sequence) - 3)
        new_sequence = list(current_sequence)
	    # Randomly choosing a new codon to replace the existing one
        new_codon = random.choice(list(codon_usage.keys()))
        new_sequence[position_to_mutate:position_to_mutate+3] = list(new_codon)
        new_sequence = ''.join(new_sequence)
        
        new_cai = calculate_cai(new_sequence, codon_usage)

	     # Updating the best sequence if the new CAI is closer to the target CAI
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
