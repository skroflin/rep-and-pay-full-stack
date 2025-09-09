export interface MyTrainingSessionRequest {
    trainingType: "push" | "pull" | "legs" | "crossfit" | "conditioning" | "yoga" | "weightlifting"
    trainingLevel: "beginner" | "intermediate" | "advanced"
    beginningOfSession: Date | string
    endOfSession: Date | string
    alreadyBooked: boolean
}