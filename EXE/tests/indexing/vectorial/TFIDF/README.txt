- empty: the vector of an empty document must be empty
- termInAll: if all term belong to all docs, the idf
                factor will be 0, and thus all the weights
- singleTerms: small test with two docs of a single test,
            correct values computed by hand
- smallCorpus: test using a slightly bigger corpus. see
        https://www.cs.upc.edu/~caim/slides/2ir_models.pdf
        for computations