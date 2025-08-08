export interface BookingRequest {
    userId: number
    trainingSessionId: number
    reservationTime: Date
    endOfReservationTime: Date
    bookingStatus: string
}