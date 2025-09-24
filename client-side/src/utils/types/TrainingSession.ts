export interface TrainingSessionRequest {
    trainerId: string,
    trainingType: "PUSH" | "PULL" | "LEGS" | "CROSSFIT" | "CONDITIONING" | "YOGA" | "WEIGHTLIFTING"
    trainingLevel: "BEGINNER" | "INTERMEDIATE" | "ADVANCED",
    isAlreadyBooked: boolean
}

export interface TrainingSessionResponse {
    id: string,
    trainerFirstName: string,
    trainerLastName: string,
    trainingType: "PUSH" | "PULL" | "LEGS" | "CROSSFIT" | "CONDITIONING" | "YOGA" | "WEIGHTLIFTING"
    trainingLevel: "BEGINNER" | "INTERMEDIATE" | "ADVANCED"
    beginningOfSession: Date | string,
    endOfSession: Date | string,
    isAlreadyBooked: boolean
}