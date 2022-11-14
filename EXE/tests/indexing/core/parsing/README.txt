- emptyQuery: error
- invalidBoost: misplaced boost operator -> error
- nonAlphaNumeric: non-alphanumeric term -> error
- manyBoosts: consecutive boost operators -> error
- correctQuery: must return a map of terms and weights,
                defaulting to 1 if not specified