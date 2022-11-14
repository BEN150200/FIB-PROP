1. VectorialModel::insert

- insertEmpty: must have an empty tfidf vector
- insertUnrelated: a document with no common terms
            with any other must not change the
            other weights and similarities
- insertExistingTerms: a document with terms already
            existing must change the weights
- insertAlreadyExisting: must not change the model
- removeNonExisting: must not change the model
- removeExising: removing a document with common terms
                must change the weights
- totallyDifferent: totally different documents, when queried,
                must not be similar to each other
- roughSimilarity: a test by eye of how the similarities should be
- queryEqual: equal docs must have 1.0 similarity

- equal/unequalPrortions: docs that only differ in proportion
    of words must have similarity 1.0

- testSimilarity: test with small corpus, see
    https://www.cs.upc.edu/~caim/slides/2ir_models.pdf