export interface CheckoutRequest {
    price: number
    month: string
}

export interface StripeCheckoutResponse {
    status: string
    message: string
    sessionId: string
    sessionUrl: string
}