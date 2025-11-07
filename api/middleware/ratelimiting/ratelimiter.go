package ratelimiting

import (
	"golang.org/x/time/rate"
)

func RateLimiter() *rate.Limiter {
	return rate.NewLimiter(2, 5)
}
