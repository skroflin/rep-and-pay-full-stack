export interface MyBookingRequest {
    trainingSessionId: number
    reservationTime: Date | string
    endOfReservationTime: Date | string
}