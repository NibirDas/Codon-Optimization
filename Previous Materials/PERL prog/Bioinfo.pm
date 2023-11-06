############################################################
#
# BeginPerlBioinfo.pm

# use BeginPerlBioinfo;



# extract_sequence_from_fasta_data
#
# A subroutine to extract FASTA sequence data from an array

sub extract_sequence_from_fasta_data {

    my(@fasta_file_data) = @_;

    use strict;
    use warnings;

    # Declare and initialize variables
    my $sequence = '';

    foreach my $line (@fasta_file_data) {

        # discard blank line
        if ($line =~ /^\s*$/) {
            next;

        # discard comment line
        } elsif($line =~ /^\s*#/) {
            next;

        # discard fasta header line
        } elsif($line =~ /^>/) {
            next;

        # keep line, add to sequence string
        } else {
            $sequence .= $line;
        }
    }

    # remove non-sequence data (in this case, whitespace) from $sequence string
    $sequence =~ s/\s//g;

    return $sequence;
}





# A Subroutine to Read FASTA Files

# get_file_data
#
# A subroutine to get data from a file given its filename

sub get_file_data {

    my($filename) = @_;

    use strict;
    use warnings;

    # Initialize variables
    my @filedata = (  );

    unless( open(GET_FILE_DATA, $filename) ) {
        print STDERR "Cannot open file \"$filename\"\n\n";
        exit;
    }

    @filedata = <GET_FILE_DATA>;

    close GET_FILE_DATA;

    return @filedata;
}








# parse_annotation
#
#  given a GenBank annotation, returns a hash  with
#   keys: the field names
#   values: the fields

sub parse_annotation {

    my($annotation) = @_; 
    my(%results) = (  );

    while( $annotation =~ /^[A-Z].*\n(^\s.*\n)*/gm ) {
        my $value = $&;
        (my $key = $value) =~ s/^([A-Z]+).*/$1/s;
        $results{$key} = $value;
    }

    return %results;
}

1;

