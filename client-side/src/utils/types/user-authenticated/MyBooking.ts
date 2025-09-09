export interface MyBookingRequest {
    trainingSessionId: string
}

export interface MyBookingResponse {
    trainingType: "push" | "pull" | "legs" | "crossfit" | "conditioning" | "yoga" | "weightlifting"
    trainingLevel: "beginner" | "intermediate" | "advanced"
    beginningOfSession: Date | string
    endOfSession: Date | string
    trainerFirstName: string
    trainerLastName: string
    bookingStatus: "accepted" | "rejected" | "pending"
}