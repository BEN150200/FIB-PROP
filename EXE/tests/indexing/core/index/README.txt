1. Index::of (factory)
- empty: an empty index must have empty inverted and direct indices
- repeatedTerms: check correct construction with terms of frequency >1
- singleTerm: check correct construction with corpus with only one term
2. Index::insert
- insert: common case, check the new document is added properly
- insertSame: inserting an already existing document must not change the index
3. Index::remove
- remove: common case, check the new document is removed properly
- removeInexisting: removing a non-existing document must not change the index