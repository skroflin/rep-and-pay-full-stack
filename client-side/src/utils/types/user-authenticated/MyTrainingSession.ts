export interface MyTrainingSessionRequest {
    trainingType: "PUSH" | "PULL" | "LEGS" | "CROSSFIT" | "CONDITIONING" | "YOGA" | "WEIGHTLIFTING"
    trainingLevel: "BEGINNER" | "INTERMEDIATE" | "ADVANCED"
    beginningOfSession: Date | string
    endOfSession: Date | string
}