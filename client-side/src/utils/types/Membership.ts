export interface Membership {
    id: number
    startDate: Date | string
    endDate: Date | string
    membershipPrice: number
    paymentDate: Date | string
    alreadyPaid: boolean
}

export interface MembershipResponse {
    firstName: string
    lastName: string
    startDate: Date | string
    endDate: Date | string
    membershipPrice: number
    paymentDate: Date | string
    alreadyPaid: boolean
}