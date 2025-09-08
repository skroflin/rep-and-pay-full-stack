export interface CheckoutRequest {
    price: number
    month: number | null
}

export interface StripeCheckoutResponse {
    status: string
    message: string
    sessionId: string
    sessionUrl: string
}