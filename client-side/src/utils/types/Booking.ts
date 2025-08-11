export interface BookingRequest {
    userId: number,
    trainingSessionId: number,
    reservationTime: Date | string,
    endOfReservationTime: Date | string,
    bookingStatus: string,
}

export interface BookingResponse {
    id: number,
    userFirstName: string,
    userLastName: string,
    trainingSessionId: number,
    reservationTime: Date | string,
    endOfReservationTime: Date | string,
    bookingStatus: string
}