export interface Membership {
    startDate: Date | string
    endDate: Date | string
    membershipPrice: number
    paymentDate: Date | string
}

export interface MembershipResponse {
    firstName: string
    lastName: string
    startDate: Date | string
    endDate: Date | string
    membershipPrice: number
    paymentDate: Date | string
}