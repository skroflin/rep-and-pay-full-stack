export interface BookingRequest {
    userId: string,
    trainingSessionId: string,
    reservationTime: Date | string,
    endOfReservationTime: Date | string,
    bookingStatus: string,
}

export interface BookingResponse {
    id: string,
    userFirstName: string,
    userLastName: string,
    trainingSessionId: string,
    reservationTime: Date | string,
    endOfReservationTime: Date | string,
    bookingStatus: string
}

export interface TrainerBookingResponse {
    bookingId: string,
    trainingSessionId: string,
    userFirstName: string,
    userLastName: string,
    trainingType: string,
    reservationTime: Date | string,
    endOfReservationTime: Date | string,
    bookingStatus: "APPROVED" | "REJECTED" | "PENDING"
}

export interface UpdateBookingStatusRequest {
    bookingStatus: "APPROVED" | "REJECTED"
}