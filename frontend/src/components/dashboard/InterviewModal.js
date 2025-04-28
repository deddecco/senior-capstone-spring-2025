import React, { useState, useEffect } from 'react';
import { X } from 'lucide-react';

const InterviewModal = ({
                            isOpen, onClose, title, interviewData, handleInputChange, handleSubmit
                        }) => {
    const [dateError, setDateError] = useState('');
    const today = new Date().toISOString().split('T')[0];
    const formatDateForDisplay = (dateString) => {
        if (!dateString) return '';

        try {
            const [year, month, day] = dateString.split('-');
            return `${month}/${day}/${year}`;
        } catch (e) {
            console.error('Error formatting date:', e);
            return dateString;
        }
    };

    // validate the date when the modal opens or when date changes
    useEffect(() => {
        if (isOpen && interviewData.date) {
            validateDate(interviewData.date);
        } else {
            // clear any error message when modal opens with no date
            setDateError('');
        }
    }, [isOpen, interviewData.date]);

    // function to validate the date is not in the past
    const validateDate = (dateValue) => {
        const selectedDate = new Date(dateValue);
        const currentDate = new Date();

        // reset the time portion for fair comparison
        currentDate.setHours(0, 0, 0, 0);

        if (selectedDate < currentDate) {
            setDateError('Please select a date that is today or in the future');
            return false;
        } else {
            setDateError('');
            return true;
        }
    };

    // modified input change handler to validate dates
    const onInputChange = (e) => {
        const { name, value } = e;

        // if changing date, validate it
        if (name === 'date') {
            validateDate(value);
        }

        // pass to the parent handler
        handleInputChange(e);
    };

    // modified submit handler to prevent submission with invalid dates
    const onSubmit = (e) => {
        e.preventDefault();

        // first validate the date
        if (!interviewData.date || validateDate(interviewData.date)) {
            // If date is valid, proceed with submission
            handleSubmit(e);
        }
    };

    if (!isOpen) return null;

    return (<div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
        <div className="bg-white rounded-lg p-6 w-full max-w-md">
            <div className="flex justify-between items-center mb-4">
                <h2 className="text-xl font-bold">{title}</h2>
                <button
                    onClick={onClose}
                    className="text-gray-500 hover:text-gray-700"
                >
                    <X className="h-5 w-5"/>
                </button>
            </div>

            <form onSubmit={onSubmit}>
                <div className="space-y-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                            Company Name
                        </label>
                        <input
                            type="text"
                            name="company"
                            value={interviewData.company || ''}
                            onChange={onInputChange}
                            required
                            className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                            Interview Format
                        </label>
                        <select
                            name="format"
                            value={interviewData.format || 'virtual'}
                            onChange={onInputChange}
                            className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                            <option value="virtual">Virtual</option>
                            <option value="in-person">In-Person</option>
                            <option value="phone">Phone</option>
                        </select>
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                            Interview Round
                        </label>
                        <select
                            name="round"
                            value={interviewData.round || 'screening'}
                            onChange={onInputChange}
                            className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                            <option value="screening">Screening</option>
                            <option value="technical">Technical</option>
                            <option value="hr">HR</option>
                            <option value="hiring manager">Hiring Manager</option>
                            <option value="final">Final</option>
                        </select>
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                            Date
                        </label>
                        <input
                            type="date"
                            name="date"
                            value={interviewData.date || ''}
                            onChange={onInputChange}
                            min={today}
                            required
                            className={`w-full rounded-md border ${dateError ? 'border-red-500' : 'border-gray-300'} px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500`}
                        />
                        {dateError && (
                            <p className="mt-1 text-xs text-red-600">{dateError}</p>
                        )}
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                            Time
                        </label>
                        <input
                            type="time"
                            name="time"
                            value={interviewData.time || ''}
                            onChange={onInputChange}
                            required
                            className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                    </div>
                </div>

                <div className="mt-6 flex justify-end gap-3">
                    <button
                        type="button"
                        onClick={onClose}
                        className="rounded-md border border-gray-300 px-4 py-2 text-sm font-medium hover:bg-gray-50"
                    >
                        Cancel
                    </button>
                    <button
                        type="submit"
                        disabled={!!dateError}
                        className="rounded-md bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700 disabled:bg-blue-300 disabled:cursor-not-allowed"
                    >
                        {title === "Add New Interview" ? "Add Interview" : "Save Changes"}
                    </button>
                </div>
            </form>
        </div>
    </div>);
};

export default InterviewModal;