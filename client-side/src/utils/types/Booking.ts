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
    trainingLevel: string,
    startOfSession: Date | string,
    endOfSession: Date | string,
    bookingStatus: "approved" | "rejected" | "pending"
}

export interface UpdateBookingStatusRequest {
    bookingStatus: "approved" | "rejected"
}