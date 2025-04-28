import React from 'react';
import {Calendar} from 'lucide-react';

const InterviewCard = ({interview, onEdit, onDelete}) => {
    // function to capitalize the first letter of each word
    const capitalize = (text) => {
        if (!text) return '';

        // handle special case for "in-person"
        if (text.toLowerCase() === 'in-person') {
            return 'In-Person';
        }

        // for other cases, capitalize first letter
        return text.charAt(0).toUpperCase() + text.slice(1);
    };

    const formatDateDisplay = (dateString) => {
        if (!dateString) return '';

        try {
            if (dateString.includes('/')) return dateString;
            const parts = dateString.split('-');
            if (parts.length === 3) {
                return `${parts[1]}-${parts[2]}-${parts[0]}`;
            }
            return dateString;
        } catch (e) {
            console.error('Error formatting date:', e);
            return dateString;
        }
    };

    return (<div className="rounded-lg border bg-white p-6 shadow-sm hover:shadow-md transition-shadow">
        <div className="flex items-start justify-between">
            <div className="flex gap-4">
                <div className="rounded-full bg-purple-100 p-3 h-12 w-12 flex items-center justify-center">
                    <Calendar className="h-6 w-6 text-purple-500"/>
                </div>
                <div>
                    <h3 className="font-medium text-lg">{interview.company}</h3>
                    <p className="text-gray-600">{capitalize(interview.round)} • {capitalize(interview.format)}</p>
                    <div className="mt-2 flex flex-wrap gap-2">
                      <span className="inline-flex items-center rounded-full bg-blue-100 px-2.5 py-0.5 text-xs font-medium text-blue-800">
                        {formatDateDisplay(interview.date)} at {interview.time}
                      </span>
                    </div>
                </div>
            </div>
            <div className="flex gap-2">
                {onEdit && (<button
                    onClick={() => onEdit(interview)}
                    className="rounded-md bg-blue-600 px-3 py-1.5 text-sm font-medium text-white hover:bg-blue-700">
                    Edit
                </button>)}
                {onDelete && (<button
                    onClick={() => onDelete(interview.id)}
                    className="rounded-md bg-red-600 px-3 py-1.5 text-sm font-medium text-white hover:bg-red-700">
                    Delete
                </button>)}
            </div>
        </div>
    </div>);
};

export default InterviewCard;