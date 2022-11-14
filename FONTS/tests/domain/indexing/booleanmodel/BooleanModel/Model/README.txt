1. BooleanModel::queryTerm

- nonExistingTerm: querying a term not in the index must return an empty set
- inAllTerm: querying a term in all the docs must return a set of them all
- termEmpty: querying any term in an empty index must return an empty set

2. BooleanModel::querySet

- nonExistingSet: querying a set of terms not in the index (none)
                    must return an empty set
- almostButNoSet: querying a set of terms in the index, but not all
                    in any one document, must return an empty set
- allSet: querying a set of terms in every document must return a set of them all

3. BooleanModel::querySequence

- nonExistingSequence: querying a sequence not in the index (in the
                        proper order, in any one document) must return
                        an empty set
- orderMatters: querying a sequence in the index must return the documents
                    containing it, whilst the same sequence, unordered, must
                    return an empty set (if the new order does not match, ofc!)
- matchSequence: any sequence that appears, in order, in some documents, must
                    return a set of them all