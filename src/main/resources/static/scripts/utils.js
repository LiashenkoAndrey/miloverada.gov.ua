export function checkFileSize(size) {
    if (size < 10485760) {
        return true;
    } else {
        sendNotification("Перевищено ліміт файлу 10MB!", 'Error')
        return false
    }
}

