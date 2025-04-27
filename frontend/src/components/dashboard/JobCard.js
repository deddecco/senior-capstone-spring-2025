import React from 'react';
import {Briefcase, Star} from 'lucide-react';

const JobCard = ({job, onToggleFavorite, onEdit, onDelete}) => {
    return (<div className="rounded-lg border bg-white p-6 shadow-sm hover:shadow-md transition-shadow">
        <div className="flex items-start justify-between">
            <div className="flex gap-4">
                <div className="rounded-full bg-blue-100 p-3 h-12 w-12 flex items-center justify-center">
                    <Briefcase className="h-6 w-6 text-blue-500"/>
                </div>
                <div>
                    <h3 className="font-medium text-lg">{job.title}</h3>
                    <p className="text-gray-600">{job.company} • {job.location}</p>
                    <div className="mt-2 flex flex-wrap gap-2">
              <span
                  className="inline-flex items-center rounded-full bg-blue-100 px-2.5 py-0.5 text-xs font-medium text-blue-800">
                {job.type || job.level}
              </span>
                        <span
                            className="inline-flex items-center rounded-full bg-green-100 px-2.5 py-0.5 text-xs font-medium text-green-800">
                {job.salary || `$${job.minSalary || 0} - $${job.maxSalary || 0}`}
              </span>
                        {job.status && (<span
                            className="inline-flex items-center rounded-full bg-purple-100 px-2.5 py-0.5 text-xs font-medium text-purple-800">
                  {job.status}
                </span>)}
                    </div>
                </div>
            </div>
            <div className="flex gap-2">
                {onEdit && (<button
                    onClick={() => onEdit(job)}
                    className="rounded-md bg-blue-600 px-3 py-1.5 text-sm font-medium text-white hover:bg-blue-700">
                    Edit
                </button>)}
                {onDelete && (<button
                    onClick={() => onDelete(job.id)}
                    className="rounded-md bg-red-600 px-3 py-1.5 text-sm font-medium text-white hover:bg-red-700">
                    Delete
                </button>)}
                <button
                    onClick={() => onToggleFavorite(job)}
                    className="rounded-md border border-gray-300 px-3 py-1.5 text-sm font-medium hover:bg-gray-50"
                >
                    <Star
                        className={`h-4 w-4 ${job.favorite ? 'fill-yellow-400 text-yellow-400' : 'text-gray-500'}`}/>
                </button>
            </div>
        </div>
        <p className="mt-4 text-sm text-gray-600">{job.description}</p>
    </div>);
};

export default JobCard;