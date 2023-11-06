#!/usr/bin/perl

 
use strict;
use warnings;
use Bioinfo;

#
# Get sequence from fasta file
#
my $fastafile = 'sample.dna';
my @file_data = get_file_data($fastafile);
my $sequence = extract_sequence_from_fasta_data(@file_data);
#

my $size = 3;
my %count = mercount($size,$sequence);

#
# Sort the keys by the count, and output results
#
my @sortedkeys = sort {$count{$b} <=> $count{$a}} keys %count;

foreach my $key (@sortedkeys) {
	print "$key ", $count{$key}, "\n";
}

exit;

################################################################################
# Subroutines
################################################################################
# mercount
#  - count all "mers" - subsequences of specified size - in a sequence
#
sub mercount {
	my($size, $seq) = @_;

	my %count = ();

	# Iterate through each subsequence
	for(my $i=0 ; $i < length($seq)-3 ; ++$i) {

		my $mer = substr($seq, $i, $size);

		if(defined $count{$mer}) {
			$count{$mer}++;
		}else{
			$count{$mer} = 1;
		}
	}

	return %count;
}

