export interface UserTrainingSessionResponse {
    trainerFirstName: string
    trainerLastName: string
    trainingType: "push" | "pull" | "legs" | "crossfit" | "conditioning" | "yoga" | "weightlifting"
    trainingLevel: "beginner" | "intermediate" | "advanced"
    startOfSession: Date | string
    endOfSession: Date | string
}