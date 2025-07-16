"use client"

import {ReactElement, useState} from "react"
import { useRouter } from "next/navigation"
import { MapPin, Eye, EyeOff, Mail, Lock, User} from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"

export default function Component() {
    const router = useRouter()

    const [isLogin, setIsLogin] = useState(true)
    const [showPassword, setShowPassword] = useState(false)
    const [showConfirmPassword, setShowConfirmPassword] = useState(false)

    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [confirmPassword, setConfirmPassword] = useState("")
    const [username, setUsername] = useState("")

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault()
        try {
            const response = await fetch("/login-api", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, password }),
            })
            if (!response.ok) throw new Error("로그인 실패");
            alert("로그인 성공! 홈페이지로 이동합니다.")

            const data = await response.json();

            localStorage.setItem("access_token", data.accessToken);
            localStorage.setItem("refresh_token", data.refreshToken);

            router.push("/main")
        } catch (err: unknown) {
            // @ts-expect-error: 로그인 실패 시
            alert("로그인 실패: " + err.message)
        }
    }

    const handleSignup = async (e: React.FormEvent) => {
        e.preventDefault()
        if (password !== confirmPassword) {
            alert("비밀번호가 일치하지 않습니다.")
            return
        }

        try {
            const response = await fetch("/joinForm", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, password, username }),
            })
            if (!response.ok) throw new Error(await response.text())
            alert("회원가입 성공! 로그인 페이지로 이동합니다.")
            router.push("/loginForm")
        } catch (err: unknown) {
            // @ts-expect-error: 회원 가입 실패 시
            alert("회원가입 실패: " + err.message)
        }
    }

    const loginWithGoogle = () => {
        window.location.href = "/oauth2/authorization/google"
    }

    const loginWithKakao = () => {
        window.location.href = "/oauth2/authorization/kakao"
    }

    return (
        <div className="min-h-screen bg-white flex items-center justify-center px-6 py-12">
            <div className="w-full max-w-md">
                <div className="text-center mb-8">
                    <div className="flex items-center justify-center space-x-3 mb-6">
                        <div className="w-12 h-12 bg-[#DCD3FF] rounded-2xl flex items-center justify-center">
                            <MapPin className="w-7 h-7 text-black" />
                        </div>
                        <h1 className="text-3xl font-bold text-black">CampusMap</h1>
                    </div>
                    <p className="text-black text-lg">{isLogin ? "다시 만나서 반가워요!" : "두루미를 시작해보세요"}</p>
                </div>

                <div className="bg-white rounded-3xl shadow-2xl border border-[#DCD3FF] p-8">
                    <div className="flex mb-8 bg-[#DCD3FF] rounded-2xl p-1">
                        <button
                            onClick={() => setIsLogin(true)}
                            className={`flex-1 py-3 px-4 rounded-xl text-sm font-semibold transition-all ${
                                isLogin ? "bg-white text-black shadow-sm" : "text-black hover:bg-white hover:bg-opacity-50"
                            }`}
                        >
                            로그인
                        </button>
                        <button
                            onClick={() => setIsLogin(false)}
                            className={`flex-1 py-3 px-4 rounded-xl text-sm font-semibold transition-all ${
                                !isLogin ? "bg-white text-black shadow-sm" : "text-black hover:bg-white hover:bg-opacity-50"
                            }`}
                        >
                            회원가입
                        </button>
                    </div>

                    {isLogin ? (
                        <form className="space-y-6" onSubmit={handleLogin}>
                            {/* 이메일 */}
                            <InputWithIcon
                                id="email"
                                label="이메일"
                                icon={<Mail />}
                                value={email}
                                setValue={setEmail}
                                type="email"
                                placeholder="이메일을 입력하세요"
                            />
                            {/* 비밀번호 */}
                            <InputWithPassword
                                id="password"
                                label="비밀번호"
                                value={password}
                                setValue={setPassword}
                                show={showPassword}
                                setShow={setShowPassword}
                            />

                            <div className="flex items-center justify-between">
                                <label className="flex items-center space-x-2">
                                    <input type="checkbox" className="w-4 h-4 border-[#DCD3FF] rounded" />
                                    <span className="text-sm text-black">로그인 상태 유지</span>
                                </label>
                                <button type="button" className="text-sm text-[#C8A2C8] hover:text-black transition-colors">
                                    비밀번호 찾기
                                </button>
                            </div>

                            <Button type="submit" className="w-full bg-[#DCD3FF] text-black h-12 text-lg font-semibold rounded-xl">
                                로그인
                            </Button>
                        </form>
                    ) : (
                        <form className="space-y-6" onSubmit={handleSignup}>
                            <InputWithIcon
                                id="username"
                                label="이름"
                                icon={<User />}
                                value={username}
                                setValue={setUsername}
                                placeholder="이름을 입력하세요"
                            />
                            <InputWithIcon
                                id="email"
                                label="이메일"
                                icon={<Mail />}
                                value={email}
                                setValue={setEmail}
                                type="email"
                                placeholder="이메일을 입력하세요"
                            />
                            <InputWithPassword
                                id="password"
                                label="비밀번호"
                                value={password}
                                setValue={setPassword}
                                show={showPassword}
                                setShow={setShowPassword}
                            />
                            <InputWithPassword
                                id="confirmPassword"
                                label="비밀번호 확인"
                                value={confirmPassword}
                                setValue={setConfirmPassword}
                                show={showConfirmPassword}
                                setShow={setShowConfirmPassword}
                            />
                            <Button type="submit" className="w-full bg-[#DCD3FF] text-black h-12 text-lg font-semibold rounded-xl">
                                회원가입
                            </Button>
                        </form>
                    )}

                    {/* 소셜 로그인 */}
                    <div className="mt-8">
                        <div className="relative">
                            <div className="absolute inset-0 flex items-center">
                                <div className="w-full border-t border-[#DCD3FF]"></div>
                            </div>
                            <div className="relative flex justify-center text-sm">
                                <span className="px-4 bg-white text-black">또는</span>
                            </div>
                        </div>

                        <div className="mt-6 space-y-3">
                            <Button type="button" onClick={loginWithGoogle} className="w-full border border-[#DCD3FF] h-12 rounded-xl">
                                구글로 {isLogin ? "로그인" : "회원가입"}
                            </Button>
                            <Button type="button" onClick={loginWithKakao} className="w-full border border-[#DCD3FF] h-12 rounded-xl">
                                카카오로 {isLogin ? "로그인" : "회원가입"}
                            </Button>
                        </div>
                    </div>
                </div>

                {/* 하단 링크 */}
                <div className="text-center mt-8">
                    <p className="text-black text-sm">
                        {isLogin ? "아직 계정이 없으신가요? " : "이미 계정이 있으신가요? "}
                        <button onClick={() => setIsLogin(!isLogin)} className="text-[#C8A2C8] hover:text-black font-semibold">
                            {isLogin ? "회원가입하기" : "로그인하기"}
                        </button>
                    </p>
                </div>
            </div>
        </div>
    )
}

type Props = {
    id: string
    label: string
    icon?: ReactElement
    value: string
    setValue: (value: string) => void
    placeholder?: string
    type?: string
}

function InputWithIcon({ id, label, icon, value, setValue, placeholder, type = "text" }: Props) {
    return (
        <div className="space-y-2">
            <Label htmlFor={id} className="text-black font-medium">
                {label}
            </Label>
            <div className="relative">
                <div className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-black">{icon}</div>
                <Input
                    id={id}
                    type={type}
                    value={value}
                    placeholder={placeholder}
                    onChange={(e) => setValue(e.target.value)}
                    className="pl-11 h-12 bg-white border-[#DCD3FF] text-black rounded-xl"
                />
            </div>
        </div>
    )
}

function InputWithPassword({
                               id,
                               label,
                               value,
                               setValue,
                               show,
                               setShow,
                           }: {
    id: string
    label: string
    value: string
    setValue: (v: string) => void
    show: boolean
    setShow: (v: boolean) => void
}) {
    return (
        <div className="space-y-2">
            <Label htmlFor={id} className="text-black font-medium">
                {label}
            </Label>
            <div className="relative">
                <Lock className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-black" />
                <Input
                    id={id}
                    type={show ? "text" : "password"}
                    placeholder="비밀번호를 입력하세요"
                    className="pl-11 pr-11 h-12 bg-white border-[#DCD3FF] text-black rounded-xl"
                    value={value}
                    onChange={(e) => setValue(e.target.value)}
                />
                <button
                    type="button"
                    onClick={() => setShow(!show)}
                    className="absolute right-3 top-1/2 transform -translate-y-1/2 text-black hover:text-[#C8A2C8] transition-colors"
                >
                    {show ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
                </button>
            </div>
        </div>
    )
}
