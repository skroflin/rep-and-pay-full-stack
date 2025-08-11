export interface BookingRequest {
    userId: number
    trainingSessionId: number
    reservationTime: Date
    endOfReservationTime: Date
    bookingStatus: string
}

export interface BookingResponse {
    id: number,
    userFirstName: string,
    userLastName: string,
    trainingSessionId: number,
    reservationTime: Date,
    endOfReservationTime: Date,
    bookingStatus: string
}