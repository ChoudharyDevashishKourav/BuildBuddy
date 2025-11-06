import React, { useState, useEffect, useRef } from 'react';
import { motion, useScroll, useTransform, AnimatePresence } from 'framer-motion';
import { Globe, Code, Zap, Users, Sparkles, Terminal, MapPin, TrendingUp } from 'lucide-react';

const gradientPalette = [
  "#2f1136", "#341234", "#3a1634", "#421c38", "#4a233e",
  "#532d46", "#5b3751", "#63435d", "#69506b", "#6e5c7a",
  "#70688a", "#707499", "#6e7ea9"
];

// Boot Sequence Component
const BootSequence = ({ onComplete }) => {
  const [lines, setLines] = useState([]);
  const bootLines = [
    "> Initializing BuildBuddy OS...",
    "> Syncing Global Hackathon Network...",
    "> Connecting to Builderverse...",
    "> Launch Successful."
  ];

  useEffect(() => {
    let currentLine = 0;
    const interval = setInterval(() => {
      if (currentLine < bootLines.length) {
        setLines(prev => [...prev, bootLines[currentLine]]);
        currentLine++;
      } else {
        clearInterval(interval);
        setTimeout(onComplete, 800);
      }
    }, 600);
    return () => clearInterval(interval);
  }, []);

  return (
    <motion.div
      initial={{ opacity: 1 }}
      exit={{ opacity: 0, scale: 1.2, filter: "blur(20px)" }}
      transition={{ duration: 0.6 }}
      className="fixed inset-0 z-50 flex items-center justify-center bg-black"
    >
      <div className="font-mono text-green-400 text-lg">
        {lines.map((line, i) => (
          <motion.div
            key={i}
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ duration: 0.3 }}
            className="mb-2"
          >
            {line}
          </motion.div>
        ))}
      </div>
    </motion.div>
  );
};

// Animated Globe Component
const AnimatedGlobe = () => {
  const [mousePos, setMousePos] = useState({ x: 0, y: 0 });

  useEffect(() => {
    const handleMouseMove = (e) => {
      setMousePos({
        x: (e.clientX / window.innerWidth - 0.5) * 20,
        y: (e.clientY / window.innerHeight - 0.5) * 20
      });
    };
    window.addEventListener('mousemove', handleMouseMove);
    return () => window.removeEventListener('mousemove', handleMouseMove);
  }, []);

  return (
    <motion.div
      className="absolute inset-0 flex items-center justify-center pointer-events-none"
      animate={{ x: mousePos.x, y: mousePos.y }}
      transition={{ type: "spring", stiffness: 50, damping: 20 }}
    >
      <motion.div
        className="relative w-96 h-96"
        animate={{ rotate: 360 }}
        transition={{ duration: 60, repeat: Infinity, ease: "linear" }}
      >
        {/* Globe rings */}
        {[...Array(5)].map((_, i) => (
          <motion.div
            key={i}
            className="absolute inset-0 rounded-full border border-purple-500/20"
            style={{
              transform: `scale(${1 + i * 0.15})`,
              borderColor: gradientPalette[i + 4]
            }}
            animate={{ opacity: [0.2, 0.5, 0.2] }}
            transition={{ duration: 3, repeat: Infinity, delay: i * 0.3 }}
          />
        ))}
        
        {/* Orbiting avatars */}
        {[...Array(8)].map((_, i) => (
          <motion.div
            key={i}
            className="absolute w-10 h-10 rounded-full bg-gradient-to-br from-purple-500 to-pink-500 flex items-center justify-center text-xs font-bold shadow-lg shadow-purple-500/50"
            style={{
              left: '50%',
              top: '50%',
              marginLeft: '-20px',
              marginTop: '-20px'
            }}
            animate={{
              x: Math.cos((i / 8) * Math.PI * 2) * 180,
              y: Math.sin((i / 8) * Math.PI * 2) * 180,
              rotate: 360
            }}
            transition={{
              duration: 20,
              repeat: Infinity,
              ease: "linear",
              delay: i * 0.1
            }}
          >
            {String.fromCharCode(65 + i)}
          </motion.div>
        ))}
        
        {/* Center glow */}
        <motion.div
          className="absolute inset-0 rounded-full bg-gradient-to-r from-purple-600 to-pink-600 opacity-20 blur-3xl"
          animate={{ scale: [1, 1.2, 1] }}
          transition={{ duration: 3, repeat: Infinity }}
        />
      </motion.div>
    </motion.div>
  );
};

// Skill Tag Component
const SkillTag = ({ skill, count, delay }) => (
  <motion.div
    initial={{ opacity: 0, y: 20 }}
    whileInView={{ opacity: 1, y: 0 }}
    transition={{ delay, duration: 0.5 }}
    whileHover={{ scale: 1.1, y: -5 }}
    className="group relative px-6 py-3 rounded-full bg-gradient-to-r from-purple-900/40 to-pink-900/40 border border-purple-500/30 cursor-pointer backdrop-blur-sm"
  >
    <span className="text-purple-200 font-semibold">{skill}</span>
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      whileHover={{ opacity: 1, y: 0 }}
      className="absolute -bottom-8 left-1/2 transform -translate-x-1/2 text-xs text-pink-400 whitespace-nowrap"
    >
      {count} teams looking
    </motion.div>
  </motion.div>
);

// Map Ping Component
const MapPing = ({ x, y, name, mode, delay }) => (
  <motion.div
    initial={{ scale: 0 }}
    animate={{ scale: 1 }}
    transition={{ delay, duration: 0.5 }}
    className="absolute cursor-pointer group"
    style={{ left: `${x}%`, top: `${y}%` }}
  >
    <motion.div
      animate={{ scale: [1, 1.5, 1], opacity: [1, 0] }}
      transition={{ duration: 2, repeat: Infinity }}
      className="absolute w-4 h-4 rounded-full bg-pink-500"
    />
    <div className="w-3 h-3 rounded-full bg-pink-400 shadow-lg shadow-pink-500/50" />
    
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      whileHover={{ opacity: 1, y: 0 }}
      className="absolute left-6 top-0 bg-black/90 backdrop-blur-sm border border-purple-500/50 rounded-lg p-3 whitespace-nowrap z-10"
    >
      <div className="text-purple-200 font-semibold text-sm">{name}</div>
      <div className="text-pink-400 text-xs">{mode}</div>
    </motion.div>
  </motion.div>
);

// User Card Component
const UserCard = ({ name, skills, delay, x, y }) => (
  <motion.div
    initial={{ opacity: 0, scale: 0 }}
    whileInView={{ opacity: 1, scale: 1 }}
    transition={{ delay, duration: 0.5 }}
    className="absolute w-48 p-4 rounded-xl bg-gradient-to-br from-purple-900/60 to-pink-900/60 border border-purple-500/30 backdrop-blur-md"
    style={{ left: `${x}%`, top: `${y}%` }}
  >
    <div className="flex items-center gap-3 mb-2">
      <div className="w-10 h-10 rounded-full bg-gradient-to-br from-purple-500 to-pink-500" />
      <span className="text-white font-semibold">{name}</span>
    </div>
    <div className="flex flex-wrap gap-1">
      {skills.map((skill, i) => (
        <span key={i} className="text-xs px-2 py-1 rounded bg-purple-500/20 text-purple-200">
          {skill}
        </span>
      ))}
    </div>
  </motion.div>
);

// Activity Ticker
const ActivityTicker = () => {
  const activities = [
    { icon: Users, text: "@Riya joined Team ByteBenders" },
    { icon: Sparkles, text: "@Aarav favorited Hack NIT" },
    { icon: TrendingUp, text: "Hackathon 'BuildX' added from Devpost" },
    { icon: Code, text: "@Maya completed her profile" },
    { icon: Zap, text: "Team CodeCrafters looking for UI/UX designer" }
  ];

  return (
    <div className="overflow-hidden py-4 bg-gradient-to-r from-purple-900/20 to-pink-900/20 border-y border-purple-500/20">
      <motion.div
        animate={{ x: [0, -2000] }}
        transition={{ duration: 30, repeat: Infinity, ease: "linear" }}
        className="flex gap-8 whitespace-nowrap"
      >
        {[...activities, ...activities, ...activities].map((activity, i) => (
          <div key={i} className="flex items-center gap-2 text-purple-200">
            <activity.icon className="w-4 h-4 text-pink-400" />
            <span>{activity.text}</span>
          </div>
        ))}
      </motion.div>
    </div>
  );
};

// Main Content Component (after boot)
const MainContent = () => {
  const { scrollYProgress } = useScroll();
  
  const bgColor1 = useTransform(scrollYProgress, [0, 0.5, 1], [gradientPalette[0], gradientPalette[6], gradientPalette[12]]);
  const bgColor2 = useTransform(scrollYProgress, [0, 0.5, 1], [gradientPalette[4], gradientPalette[8], gradientPalette[10]]);

  const skills = [
    { name: "React", count: 23 },
    { name: "UI/UX", count: 18 },
    { name: "Python", count: 31 },
    { name: "ML", count: 15 },
    { name: "Blockchain", count: 12 },
    { name: "AR/VR", count: 8 }
  ];

  const hackathons = [
    { name: "BuildX", mode: "Online - 12h", x: 20, y: 30 },
    { name: "HackNIT", mode: "Hybrid - Bangalore", x: 70, y: 50 },
    { name: "DevFest", mode: "In-person - Mumbai", x: 45, y: 40 },
    { name: "CodeJam", mode: "Online - 2 days", x: 85, y: 25 }
  ];

  const users = [
    { name: "Alex", skills: ["React", "Node"], x: 15, y: 20 },
    { name: "Priya", skills: ["UI/UX", "Figma"], x: 70, y: 15 },
    { name: "Jordan", skills: ["Python", "ML"], x: 40, y: 50 },
    { name: "Maya", skills: ["Blockchain"], x: 65, y: 60 }
  ];

  return (
    <div className="relative min-h-screen bg-black text-white overflow-hidden">
      {/* Animated Background */}
      <motion.div
        className="fixed inset-0 -z-10"
        style={{
          background: useTransform(
            scrollYProgress,
            [0, 0.5, 1],
            [
              `radial-gradient(circle at 50% 50%, ${gradientPalette[4]}, ${gradientPalette[0]})`,
              `radial-gradient(circle at 30% 70%, ${gradientPalette[8]}, ${gradientPalette[2]})`,
              `radial-gradient(circle at 70% 30%, ${gradientPalette[12]}, ${gradientPalette[6]})`
            ]
          )
        }}
      />

      {/* Grid Overlay */}
      <div className="fixed inset-0 -z-10 opacity-10"
        style={{
          backgroundImage: 'linear-gradient(rgba(139, 92, 246, 0.3) 1px, transparent 1px), linear-gradient(90deg, rgba(139, 92, 246, 0.3) 1px, transparent 1px)',
          backgroundSize: '50px 50px'
        }}
      />

      {/* Hero Section */}
      <section className="relative h-screen flex items-center justify-center overflow-hidden">
        <AnimatedGlobe />
        
        <motion.div
          initial={{ opacity: 0, y: 50 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.5, duration: 1 }}
          className="relative z-10 text-center px-4"
        >
          <motion.h1
            className="text-7xl md:text-8xl font-black mb-6 bg-gradient-to-r from-purple-400 via-pink-400 to-purple-400 bg-clip-text text-transparent"
            animate={{ backgroundPosition: ['0%', '100%', '0%'] }}
            transition={{ duration: 5, repeat: Infinity }}
            style={{ backgroundSize: '200%' }}
          >
            Find Your People.
            <br />
            Build Something Wild.
          </motion.h1>
          
          <motion.p
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 1 }}
            className="text-xl text-purple-200 mb-12"
          >
            The global platform for developers, designers, and innovators.
          </motion.p>
          
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 1.3 }}
            className="flex gap-4 justify-center flex-wrap"
          >
            <motion.button
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
              className="group relative px-8 py-4 rounded-full bg-gradient-to-r from-purple-600 to-pink-600 font-bold text-lg overflow-hidden"
            >
              <motion.div
                className="absolute inset-0 bg-gradient-to-r from-pink-600 to-purple-600 opacity-0 group-hover:opacity-100"
                animate={{ rotate: 360 }}
                transition={{ duration: 3, repeat: Infinity, ease: "linear" }}
              />
              <span className="relative z-10">Enter the Grid</span>
            </motion.button>
            
            <motion.button
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
              className="px-8 py-4 rounded-full border-2 border-purple-500 font-bold text-lg hover:bg-purple-500/20"
            >
              Form a Team
            </motion.button>
          </motion.div>
        </motion.div>
      </section>

      {/* Skill Matrix */}
      <section className="relative py-32 px-4">
        <motion.div
          initial={{ opacity: 0 }}
          whileInView={{ opacity: 1 }}
          transition={{ duration: 1 }}
          className="max-w-6xl mx-auto text-center"
        >
          <h2 className="text-5xl font-bold mb-6 bg-gradient-to-r from-purple-400 to-pink-400 bg-clip-text text-transparent">
            Discover your match through skills — not just luck.
          </h2>
          
          <div className="flex flex-wrap justify-center gap-4 mt-16">
            {skills.map((skill, i) => (
              <SkillTag key={skill.name} skill={skill.name} count={skill.count} delay={i * 0.1} />
            ))}
          </div>
          
          <motion.button
            initial={{ opacity: 0 }}
            whileInView={{ opacity: 1 }}
            transition={{ delay: 0.8 }}
            whileHover={{ scale: 1.05 }}
            className="mt-16 px-8 py-4 rounded-full bg-gradient-to-r from-purple-600/40 to-pink-600/40 border border-purple-500/50 backdrop-blur-sm font-semibold"
          >
            Sync your skills → Create your profile
          </motion.button>
        </motion.div>
      </section>

      {/* Live Hackathon Map */}
      <section className="relative py-32 px-4">
        <motion.div
          initial={{ opacity: 0 }}
          whileInView={{ opacity: 1 }}
          className="max-w-6xl mx-auto"
        >
          <h2 className="text-5xl font-bold text-center mb-16 bg-gradient-to-r from-purple-400 to-pink-400 bg-clip-text text-transparent">
            Live Hackathon Map
          </h2>
          
          <div className="relative h-96 rounded-2xl bg-gradient-to-br from-purple-900/20 to-pink-900/20 border border-purple-500/30 overflow-hidden backdrop-blur-sm">
            {/* World map SVG placeholder */}
            <div className="absolute inset-0 opacity-20">
              <svg viewBox="0 0 100 50" className="w-full h-full">
                <path d="M10,20 Q30,10 50,20 T90,20" stroke="#8b5cf6" fill="none" strokeWidth="0.2" />
                <path d="M10,30 Q30,25 50,30 T90,30" stroke="#8b5cf6" fill="none" strokeWidth="0.2" />
              </svg>
            </div>
            
            {hackathons.map((h, i) => (
              <MapPing key={i} {...h} delay={i * 0.2} />
            ))}
          </div>
        </motion.div>
      </section>

      {/* Meet Your Match */}
      <section className="relative py-32 px-4">
        <motion.div
          initial={{ opacity: 0 }}
          whileInView={{ opacity: 1 }}
          className="max-w-6xl mx-auto"
        >
          <h2 className="text-5xl font-bold text-center mb-16 bg-gradient-to-r from-purple-400 to-pink-400 bg-clip-text text-transparent">
            Because solo hacks aren't solo forever.
          </h2>
          
          <div className="relative h-96">
            {users.map((user, i) => (
              <UserCard key={i} {...user} delay={i * 0.2} />
            ))}
            
            {/* Connection lines */}
            <svg className="absolute inset-0 pointer-events-none">
              <motion.line
                x1="15%" y1="25%" x2="70%" y2="20%"
                stroke="url(#gradient1)" strokeWidth="2"
                initial={{ pathLength: 0 }}
                whileInView={{ pathLength: 1 }}
                transition={{ duration: 1, delay: 0.5 }}
              />
              <motion.line
                x1="70%" y1="20%" x2="40%" y2="55%"
                stroke="url(#gradient1)" strokeWidth="2"
                initial={{ pathLength: 0 }}
                whileInView={{ pathLength: 1 }}
                transition={{ duration: 1, delay: 0.8 }}
              />
              <defs>
                <linearGradient id="gradient1">
                  <stop offset="0%" stopColor="#a855f7" />
                  <stop offset="100%" stopColor="#ec4899" />
                </linearGradient>
              </defs>
            </svg>
          </div>
        </motion.div>
      </section>

      {/* Community Pulse */}
      <section className="py-16">
        <h2 className="text-4xl font-bold text-center mb-8 bg-gradient-to-r from-purple-400 to-pink-400 bg-clip-text text-transparent">
          Community Pulse
        </h2>
        <ActivityTicker />
      </section>

      {/* Final CTA */}
      <section className="relative py-32 px-4 min-h-screen flex items-center justify-center">
        <motion.div
          className="absolute inset-0"
          animate={{
            background: [
              'radial-gradient(circle at 50% 50%, rgba(139, 92, 246, 0.1), transparent 70%)',
              'radial-gradient(circle at 50% 50%, rgba(236, 72, 153, 0.1), transparent 70%)',
              'radial-gradient(circle at 50% 50%, rgba(139, 92, 246, 0.1), transparent 70%)'
            ]
          }}
          transition={{ duration: 4, repeat: Infinity }}
        />
        
        <motion.div
          initial={{ opacity: 0, scale: 0.8 }}
          whileInView={{ opacity: 1, scale: 1 }}
          transition={{ duration: 1 }}
          className="relative z-10 text-center"
        >
          <h2 className="text-6xl font-bold mb-6">
            You've seen the network.
            <br />
            <span className="bg-gradient-to-r from-purple-400 to-pink-400 bg-clip-text text-transparent">
              Now join it.
            </span>
          </h2>
          
          <div className="flex gap-4 justify-center mt-12 flex-wrap">
            <motion.button
              whileHover={{ scale: 1.05, boxShadow: '0 0 40px rgba(139, 92, 246, 0.6)' }}
              whileTap={{ scale: 0.95 }}
              className="px-10 py-5 rounded-full bg-gradient-to-r from-purple-600 to-pink-600 font-bold text-xl"
            >
              Create Profile
            </motion.button>
            
            <motion.button
              whileHover={{ scale: 1.05, boxShadow: '0 0 40px rgba(236, 72, 153, 0.6)' }}
              whileTap={{ scale: 0.95 }}
              className="px-10 py-5 rounded-full border-2 border-purple-500 font-bold text-xl hover:bg-purple-500/20"
            >
              Explore Hackathons
            </motion.button>
          </div>
        </motion.div>
      </section>

      {/* Footer */}
      <footer className="relative py-8 border-t border-purple-500/20 text-center text-purple-300">
        <motion.div
          initial={{ opacity: 0 }}
          whileInView={{ opacity: 1 }}
          className="font-mono text-sm"
        >
          <motion.span
            animate={{ opacity: [1, 0.5, 1] }}
            transition={{ duration: 2, repeat: Infinity }}
          >
            _
          </motion.span>
          BuildBuddy OS v1.0.0 | Powered by the Builderverse
        </motion.div>
      </footer>
    </div>
  );
};

// Main Landing Page Component
export default function BuildBuddyLanding() {
  const [bootComplete, setBootComplete] = useState(false);

  return (
    <>
      {!bootComplete && (
        <AnimatePresence>
          <BootSequence onComplete={() => setBootComplete(true)} />
        </AnimatePresence>
      )}
      
      {bootComplete && <MainContent />}
    </>
  );
}