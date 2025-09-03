export interface CheckoutRequest {
    price: number
}

export interface StripeCheckoutResponse {
    status: string
    message: string
    sessionId: string
    sessionUrl: string
}