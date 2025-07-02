import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Main from './main';
import NoticeList from './noticeList';


function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/main" element={<Main />} />
                <Route path="/notice" element={<NoticeList />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;