export interface BookingRequest {
    userId: string,
    trainingSessionId: string,
    bookingStatus: string,
}

export interface BookingResponse {
    id: string,
    userFirstName: string,
    userLastName: string,
    trainingSessionId: string,
    bookingStatus: string
}

export interface TrainerBookingResponse {
    bookingId: string,
    trainingSessionId: string,
    userFirstName: string,
    userLastName: string,
    trainingType: string,
    beginningOfSession: Date | string,
    endOfSession: Date | string,
    bookingStatus: "APPROVED" | "REJECTED" | "PENDING"
}

export interface UpdateBookingStatusRequest {
    bookingStatus: "APPROVED" | "REJECTED"
}