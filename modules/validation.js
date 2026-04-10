// validateFields a helper function to check for missing or empty fields in request body
export const validateFields = (fields) => {
    // Loop through each field and check if it's missing or empty
    for (const [field, value] of Object.entries(fields)) {
        // Check if value is missing or empty (for strings, also check if it's just whitespace)
        if (!value || (typeof value === 'string' && value.trim() === '')) {
        
        // Regex to convert camelCase field names to human-readable format (e.g., "fullName" -> "Full Name")
        const fieldName = field.replace(/([A-Z])/g, ' $1').toLowerCase();
        
        // Return an error message indicating which field is missing
        return `${fieldName.charAt(0).toUpperCase() + fieldName.slice(1).trim()} is required`;
        }
    }
    return null;
};
