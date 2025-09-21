export interface CheckoutRequest {
    price: number
    month: number
}

export interface StripeCheckoutResponse {
    status: string
    message: string
    sessionId: string
    sessionUrl: string
}