export interface TrainingSessionRequest {
    trainerId: string,
    trainingType: string,
    trainingLevel: string,
}

export interface TrainingSessionResponse {
    id: string,
    trainerFirstName: string,
    trainerLastName: string,
    trainingType: string,
    trainingLevel: string,
    beginningOfSession: Date | string,
    endOfSession: Date | string
}