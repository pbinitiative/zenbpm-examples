# Test Data

## OK case

A well-specified commission payout request with a clear business reason and a significant amount:

```json
{
  "reason": "Uzavřel jsem obchod v hodnotě 1 000 000 Kč se ziskem 100 000 Kč pro klienta Josefa Nováka.",
  "amount": 1000000
}
```

## NOT OK case

A vague reason with a small amount — the AI pre-check should reject this:

```json
{
  "reason": "Protože si to zasloužím.",
  "amount": 10000
}
```
