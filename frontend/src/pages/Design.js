import React from "react";

function Design() {
    return (
        <div className="min-h-screen bg-gray-100 p-8">
            <h1 className="text-4xl font-bold text-center mb-12">Design Playground</h1>

            {/* Buttons */}
            <section className="mb-12">
                <h2 className="text-3xl font-semibold mb-4">Buttons</h2>
                <div className="flex flex-wrap gap-4">
                    <button className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">
                        Primary Button
                    </button>
                    <button className="px-4 py-2 bg-gray-200 text-gray-800 rounded hover:bg-gray-300">
                        Secondary Button
                    </button>
                    <button className="px-4 py-2 bg-green-500 text-white rounded-full hover:bg-green-600">
                        Rounded Button
                    </button>
                    <button className="px-4 py-2 border border-blue-500 text-blue-500 rounded hover:bg-blue-50">
                        Outline Button
                    </button>
                    <button className="px-4 py-2 bg-red-500 text-white rounded-md hover:bg-red-600">
                        Danger Button
                    </button>
                </div>
            </section>

            {/* Typography */}
            <section className="mb-12">
                <h2 className="text-3xl font-semibold mb-4">Typography</h2>
                <div className="space-y-4">
                    <p className="text-base">
                        This is <span className="font-bold">body text</span> with some emphasis.
                    </p>
                    <p className="text-xl">This is larger text suitable for subheadings.</p>
                    <p className="text-2xl font-bold">This is even larger, bold text.</p>
                    <p className="text-sm italic">This is small italic text.</p>
                    <p className="text-4xl font-extrabold">This is a large display heading.</p>
                </div>
            </section>

            {/* Cards */}
            <section className="mb-12">
                <h2 className="text-3xl font-semibold mb-4">Cards</h2>
                <div className="flex flex-wrap gap-4">
                    <div className="w-64 p-4 bg-white shadow rounded">
                        <h3 className="text-xl font-bold mb-2">Card Title</h3>
                        <p className="text-sm text-gray-600">This is some example text for the card content.</p>
                    </div>
                    <div className="w-64 p-4 bg-blue-100 shadow rounded">
                        <h3 className="text-xl font-bold mb-2">Card Title</h3>
                        <p className="text-sm text-gray-600">This card has a blue background variation.</p>
                    </div>
                    <div className="w-64 p-4 bg-green-100 shadow rounded">
                        <h3 className="text-xl font-bold mb-2">Card Title</h3>
                        <p className="text-sm text-gray-600">This card has a green background variation.</p>
                    </div>
                </div>
            </section>

            {/* Form Elements */}
            <section className="mb-12">
                <h2 className="text-3xl font-semibold mb-4">Form Elements</h2>
                <div className="space-y-4">
                    <input
                        type="text"
                        placeholder="Text Input"
                        className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                    <textarea
                        placeholder="Textarea"
                        rows="3"
                        className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                    ></textarea>
                    <select className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <option>Option 1</option>
                        <option>Option 2</option>
                        <option>Option 3</option>
                    </select>
                </div>
            </section>

            {/* Badges & Labels */}
            <section className="mb-12">
                <h2 className="text-3xl font-semibold mb-4">Badges & Labels</h2>
                <div className="flex flex-wrap gap-4">
                    <span className="px-2 py-1 bg-blue-500 text-white rounded">Primary Badge</span>
                    <span className="px-2 py-1 bg-green-500 text-white rounded">Success Badge</span>
                    <span className="px-2 py-1 bg-red-500 text-white rounded">Danger Badge</span>
                    <span className="px-2 py-1 bg-gray-500 text-white rounded">Secondary Badge</span>
                </div>
            </section>

            {/* Links */}
            <section>
                <h2 className="text-3xl font-semibold mb-4">Links</h2>
                <div className="flex flex-wrap gap-4">
                    <a href="#!" className="text-blue-500 hover:underline">
                        Standard Link
                    </a>
                    <a href="#!" className="text-green-500 hover:underline">
                        Green Link
                    </a>
                    <a href="#!" className="text-red-500 hover:underline">
                        Red Link
                    </a>
                </div>
            </section>
        </div>
    );
}

export default Design;
